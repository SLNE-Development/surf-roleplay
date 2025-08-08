package dev.slne.surf.roleplay.core.player.license

import com.github.shynixn.mccoroutine.folia.launch
import com.google.auto.service.AutoService
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.textArgument
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.RpPlayerManager
import dev.slne.surf.roleplay.api.player.identity.RpIdentity
import dev.slne.surf.roleplay.api.player.license.IdentityLicense
import dev.slne.surf.roleplay.api.player.license.License
import dev.slne.surf.roleplay.api.player.license.LicenseService
import dev.slne.surf.roleplay.api.player.license.utils.LicenseRemovedReason
import dev.slne.surf.roleplay.core.player.license.db.IdentityLicenseModel
import dev.slne.surf.roleplay.core.player.license.db.IdentityLicenseTable
import dev.slne.surf.roleplay.core.player.license.licenses.civilian.CivilianFishingLicenseImpl
import dev.slne.surf.roleplay.core.player.license.licenses.civilian.CivilianVehicleLicenseImpl
import dev.slne.surf.roleplay.core.player.license.licenses.civilian.CivilianWeaponLicenseImpl
import dev.slne.surf.roleplay.core.player.license.listeners.LicenseBuyHandler
import dev.slne.surf.roleplay.core.player.license.listeners.LicenseChangedHandler
import dev.slne.surf.roleplay.core.player.rpPlayerManagerImpl
import dev.slne.surf.roleplay.core.plugin
import dev.slne.surf.surfapi.bukkit.api.event.register
import dev.slne.surf.surfapi.core.api.messages.adventure.key
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.toObjectList
import kotlinx.coroutines.Dispatchers
import net.kyori.adventure.key.Key
import net.kyori.adventure.util.Services
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

@AutoService(LicenseService::class)
class LicenseServiceImpl : LicenseService, Services.Fallback {

    suspend fun onLoad() {
        _licenses.add(CivilianFishingLicenseImpl)

        CivilianWeaponLicenseImpl.register { _licenses.add(it) }
        CivilianVehicleLicenseImpl.register { _licenses.add(it) }
    }

    suspend fun onEnable() {
        LicenseExpirationJob.start()

        LicenseChangedHandler.register()
        LicenseBuyHandler.register()

        LicenseNpc.spawnNpc()

        commandAPICommand("give-license") {
            textArgument("licenseName") {
                replaceSuggestions(
                    ArgumentSuggestions.stringCollection {
                        licenses.map { it.key.asString() }
                    }
                )
            }

            playerExecutor { player, args ->
                val licenseName: String by args
                val namespace = licenseName.substringBefore(":")
                val value = licenseName.substringAfter(":")
                val license = LicenseService.getLicenseByKey(key(namespace, value))

                if (license == null) {
                    player.sendText {
                        appendPrefix()

                        error("Die Lizenz ")
                        variableValue(licenseName)
                        error(" konnte nicht gefunden werden.")
                    }

                    return@playerExecutor
                }

                plugin.launch {
                    RpPlayer[player.uniqueId].addLicense(license)
                }
            }
        }
    }

    suspend fun onDisable() {
        LicenseExpirationJob.stop()
    }

    private val _licenses = mutableObjectSetOf<License>()
    override val licenses get() = _licenses.freeze()

    suspend fun fetchLicenses(identity: RpIdentity) = newSuspendedTransaction(Dispatchers.IO) {
        val rpPlayerModel = rpPlayerManagerImpl.findOrCreate(identity.player.uuid)

        IdentityLicenseModel.find {
            (IdentityLicenseTable.player eq rpPlayerModel.id) and
                    (IdentityLicenseTable.identity eq identity.type)
        }.map { it.toApi(identity) }.toObjectList()
    }

    override fun getLicense(license: Class<out License>) =
        _licenses.firstOrNull { license.isAssignableFrom(it::class.java) }

    override fun getLicenseOrThrow(license: Class<out License>) =
        getLicense(license) ?: error("License not found: ${license.name}")

    override fun getLicenseByKey(key: Key) = _licenses.firstOrNull { it.key == key }

    override fun getLicenseByKeyOrThrow(key: Key) =
        getLicenseByKey(key) ?: error("License not found: $key")

    override suspend fun createLicense(
        identityLicense: IdentityLicense
    ) = newSuspendedTransaction(Dispatchers.IO) {
        val (license, identity, expiresAt, createdAt) = identityLicense
        val rpPlayerModel = rpPlayerManagerImpl.findOrCreate(identity.player.uuid)

        IdentityLicenseModel.new {
            this.player = rpPlayerModel
            this.identity = identity.type
            this.license = license.key.asString()
            this.expiresAt = expiresAt
            this.createdAt = createdAt
        }.toApi(identityLicense.identity)
    }

    override suspend fun confiscateLicense(
        identity: RpIdentity,
        license: License,
        confiscatedBy: RpPlayer,
        confiscatedReason: String
    ) = newSuspendedTransaction(Dispatchers.IO) {
        val player = identity.player
        val parentLicense = player.getIdentity(identity.type)?.getLicense(license::class.java)
            ?: return@newSuspendedTransaction false

        val parentResult = player.removeLicense(
            license,
            LicenseRemovedReason.Confiscated(confiscatedBy, confiscatedReason)
        )

        val childrenResults = license.children.map { childLicense ->
            if (player.hasLicense(childLicense)) {
                player.removeLicense(
                    childLicense,
                    LicenseRemovedReason.ConfiscatedChild(
                        parentLicense,
                        confiscatedBy,
                        confiscatedReason
                    )
                )
                return@map true
            }

            false
        }

        parentResult && childrenResults.all { it }
    }

    override suspend fun removeLicense(
        identity: RpIdentity,
        license: License
    ) = newSuspendedTransaction(Dispatchers.IO) {
        val player = identity.player
        val rpPlayerModel = rpPlayerManagerImpl.findOrCreate(player.uuid)
        val playerLicense = IdentityLicenseModel.find {
            (IdentityLicenseTable.player eq rpPlayerModel.id) and
                    (IdentityLicenseTable.identity eq identity.type) and
                    (IdentityLicenseTable.license eq license.key.asString())
        }

        if (playerLicense.empty()) {
            return@newSuspendedTransaction false
        }

        playerLicense.forEach { it.delete() }

        true
    }

    fun getAllExpiredLicenses() = RpPlayerManager.players
        .mapNotNull {
            val activeIdentity = it.activeIdentity ?: return@mapNotNull null

            activeIdentity to activeIdentity.licenses.filter { license -> license.isExpired }
        }.toObjectList()
}

val licenseServiceImpl get() = LicenseService.INSTANCE as LicenseServiceImpl
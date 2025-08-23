package dev.slne.surf.roleplay.server.player.license

import com.google.auto.service.AutoService
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.RpPlayerManager
import dev.slne.surf.roleplay.api.player.identity.RpIdentity
import dev.slne.surf.roleplay.api.player.license.IdentityLicense
import dev.slne.surf.roleplay.api.player.license.License
import dev.slne.surf.roleplay.api.player.license.LicenseService
import dev.slne.surf.roleplay.api.player.license.utils.LicenseRemovedReason
import dev.slne.surf.roleplay.core.player.rpPlayerManagerImpl
import dev.slne.surf.roleplay.server.player.license.db.IdentityLicenseModel
import dev.slne.surf.roleplay.server.player.license.db.IdentityLicenseTable
import dev.slne.surf.surfapi.core.api.messages.adventure.key
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
package dev.slne.surf.roleplay.mechanic.mechanics.license

import com.github.shynixn.mccoroutine.folia.launch
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.textArgument
import dev.slne.surf.npc.api.dsl.npc
import dev.slne.surf.npc.api.surfNpcApi
import dev.slne.surf.roleplay.api.mechanic.MechanicRegistry
import dev.slne.surf.roleplay.api.mechanic.getMechanic
import dev.slne.surf.roleplay.api.mechanic.license.License
import dev.slne.surf.roleplay.api.mechanic.license.LicenseMechanic
import dev.slne.surf.roleplay.api.mechanic.license.player.LicensePlayer
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.mechanic.MechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanicRegistryImpl
import dev.slne.surf.roleplay.mechanic.mechanics.license.db.PlayerLicenseTable
import dev.slne.surf.roleplay.mechanic.mechanics.license.licenses.FishingLicenseImpl
import dev.slne.surf.roleplay.mechanic.mechanics.license.licenses.VehicleLicenseImpl
import dev.slne.surf.roleplay.mechanic.mechanics.license.licenses.WeaponLicenseImpl
import dev.slne.surf.roleplay.mechanic.mechanics.license.listeners.LicenseBuyHandler
import dev.slne.surf.roleplay.mechanic.mechanics.license.listeners.LicenseChangedHandler
import dev.slne.surf.roleplay.mechanic.mechanics.license.listeners.LicensePlayerHandler
import dev.slne.surf.roleplay.mechanic.mechanics.license.player.licensePlayer
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.core.api.messages.adventure.key
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import net.kyori.adventure.key.Key
import org.jetbrains.exposed.sql.Table

object LicenseMechanicImpl : MechanicImpl(
    "LicenseMechanic",
    handlers = objectSetOf(
        LicensePlayerHandler,
        LicenseChangedHandler,
        LicenseBuyHandler
    ),
    rpJobs = objectSetOf(
        LicenseExpirationJob
    )
), LicenseMechanic {

    const val NPC_NAME = "license_npc"

    private val _licenses = mutableObjectSetOf<License>()
    override val licenses get() = _licenses.freeze()

    override fun getDatabaseTables() = objectSetOf<Table>(
        PlayerLicenseTable
    )

    override suspend fun onLoad() {
        _licenses.add(FishingLicenseImpl)

        WeaponLicenseImpl.register { _licenses.add(it) }
        VehicleLicenseImpl.register { _licenses.add(it) }
    }

    override suspend fun onEnable() {
        commandAPICommand("give-license") {
            textArgument("licenseName") {
                replaceSuggestions(
                    ArgumentSuggestions.stringCollection {
                        MechanicRegistry.getMechanic<LicenseMechanic>().licenses.map { it.key.asString() }
                    }
                )
            }

            playerExecutor { player, args ->
                val licenseName: String by args
                val namespace = licenseName.substringBefore(":")
                val value = licenseName.substringAfter(":")
                val license = getLicenseByKey(key(namespace, value))

                plugin.launch {
                    val rpPlayer = RpPlayer[player.uniqueId]

                    rpPlayer.licensePlayer().addLicense(license)
                }
            }

            spawnNpc()
        }
    }

    private suspend fun spawnNpc() {
        val npcSkin = surfNpcApi.getSkin("CastCrafter")

        npc(mechanicRegistryImpl.plugin) {
            uniqueName = NPC_NAME
            displayName = {
                primary("Lizenzhändler")
            }

            skin = npcSkin

            location {
                world = "world"
                x = 0.0
                y = 100.0
                z = 0.0
            }
        }
    }

    override suspend fun confiscateLicense(
        player: LicensePlayer,
        license: License,
        confiscatedBy: RpPlayer,
        confiscatedReason: String
    ) = LicenseService.confiscateLicense(player, license, confiscatedBy, confiscatedReason)

    override fun getLicense(license: Class<License>) =
        licenses.firstOrNull { license.isAssignableFrom(it.javaClass) }
            ?: throw IllegalArgumentException("License of type ${license.simpleName} not found")

    override fun getLicenseByKey(name: Key) =
        licenses.firstOrNull { it.key == name }
            ?: throw IllegalArgumentException("License with key $name not found")
}

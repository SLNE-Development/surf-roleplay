package dev.slne.surf.roleplay.mechanic.mechanics.license

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.folia.launch
import com.google.auto.service.AutoService
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.textArgument
import dev.slne.surf.roleplay.api.mechanic.license.License
import dev.slne.surf.roleplay.api.mechanic.license.LicenseMechanic
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.mechanic.MechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanics.license.db.PlayerLicenseTable
import dev.slne.surf.roleplay.mechanic.mechanics.license.licenses.DriversLicenseImpl
import dev.slne.surf.roleplay.mechanic.mechanics.license.licenses.FishingLicenseImpl
import dev.slne.surf.roleplay.mechanic.mechanics.license.listeners.LicensePlayerHandler
import dev.slne.surf.roleplay.mechanic.mechanics.license.listeners.LicenseRemovedHandler
import dev.slne.surf.roleplay.mechanic.mechanics.license.player.licensePlayer
import dev.slne.surf.surfapi.core.api.messages.adventure.key
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import net.kyori.adventure.key.Key
import net.kyori.adventure.util.Services
import org.jetbrains.exposed.sql.Table
import kotlin.time.Duration.Companion.seconds

@AutoService(LicenseMechanic::class)
class LicenseMechanicImpl : MechanicImpl(
    "LicenseMechanic",
    handlers = objectSetOf(
        LicensePlayerHandler,
        LicenseRemovedHandler
    )
), LicenseMechanic, Services.Fallback {

    private val _licenses = mutableObjectSetOf<License>()
    override val licenses get() = _licenses.freeze()

    private lateinit var expirationChecker: LicenseExpirationJob

    override fun getDatabaseTables() = objectSetOf<Table>(
        PlayerLicenseTable
    )

    override fun onLoad(plugin: SuspendingJavaPlugin) {
        _licenses.add(DriversLicenseImpl)
        _licenses.add(FishingLicenseImpl)
    }

    override fun onEnable(plugin: SuspendingJavaPlugin) {
        commandAPICommand("give-license") {
            textArgument("licenseName") {
                replaceSuggestions(
                    ArgumentSuggestions.stringCollection {
                        LicenseMechanic.licenses.map { it.key.asString() }
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
        }

        expirationChecker = LicenseExpirationJob(
            delay = 1.seconds,
            plugin = plugin
        )
        expirationChecker.start()
    }

    override fun onDisable(plugin: SuspendingJavaPlugin) {
        expirationChecker.stop()
    }

    override fun getLicense(license: Class<out License>) =
        licenses.firstOrNull { it::class.java == license }
            ?: throw IllegalArgumentException("License of type ${license.simpleName} not found")

    override fun getLicenseByKey(name: Key) =
        licenses.firstOrNull { it.key == name }
            ?: throw IllegalArgumentException("License with key $name not found")
}

val licenseMechanicImpl get() = LicenseMechanic.INSTANCE as LicenseMechanicImpl
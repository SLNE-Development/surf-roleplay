package dev.slne.surf.roleplay.mechanic.mechanics.license

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.folia.launch
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.multiLiteralArgument
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.surf.roleplay.api.events.RpPlayerQuitEvent
import dev.slne.surf.roleplay.api.mechanic.license.License
import dev.slne.surf.roleplay.api.mechanic.license.LicenseMechanic
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.mechanic.MechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanics.license.db.PlayerLicenseTable
import dev.slne.surf.roleplay.mechanic.mechanics.license.licenses.DriversLicenseImpl
import dev.slne.surf.roleplay.mechanic.mechanics.license.player.LicensePlayerManager
import dev.slne.surf.roleplay.mechanic.mechanics.license.player.licensePlayer
import dev.slne.surf.surfapi.core.api.messages.adventure.key
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import net.kyori.adventure.key.Key
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.jetbrains.exposed.sql.Table
import kotlin.time.Duration.Companion.seconds

object LicenseMechanicImpl : MechanicImpl(
    "LicenseMechanic",
    handlers = objectSetOf(LicenseMechanicHandler)
), LicenseMechanic, Listener {

    private val licenses = mutableObjectSetOf<License>()

    private lateinit var expirationChecker: LicenseExpirationJob

    override fun getDatabaseTables() = objectSetOf<Table>(
        PlayerLicenseTable
    )

    override fun onLoad(plugin: SuspendingJavaPlugin) {
        licenses.add(DriversLicenseImpl)
    }

    override fun onEnable(plugin: SuspendingJavaPlugin) {
        commandAPICommand("give-license") {
            multiLiteralArgument(
                "licenseName",
                *licenses.map { it.key.asString().replaceFirst(":", "_") }.toTypedArray()
            )

            playerExecutor { player, args ->
                val licenseName: String by args
                val license = getLicenseByKey(key(licenseName.replace("_", ":")))

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
//        job = expirationChecker.start()
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

    object LicenseMechanicHandler : Listener {
        @EventHandler
        fun onPlayerQuit(event: RpPlayerQuitEvent) {
            LicensePlayerManager.remove(event.rpPlayer.uuid)
        }
    }
}
package dev.slne.surf.roleplay.paper

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.folia.launch
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.surf.roleplay.api.player.rpPlayer
import dev.slne.surf.roleplay.core.RpCore
import dev.slne.surf.roleplay.core.RpDatabase
import dev.slne.surf.roleplay.core.player.license.licenseServiceImpl
import dev.slne.surf.roleplay.mechanic.mechanicRegistryImpl
import dev.slne.surf.roleplay.paper.listeners.ListenerManager
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import org.bukkit.plugin.java.JavaPlugin

val plugin get() = JavaPlugin.getPlugin(SurfRoleplay::class.java)

class SurfRoleplay : SuspendingJavaPlugin() {

    private lateinit var rpDatabase: RpDatabase

    override suspend fun onLoadAsync() {
        RpCore.plugin = this
        rpDatabase = RpDatabase(dataPath)

        mechanicRegistryImpl.registerMechanics(this)
        mechanicRegistryImpl.registerDatabaseTables(rpDatabase)

        licenseServiceImpl.onLoad()

        rpDatabase.onLoad()

        mechanicRegistryImpl.loadMechanics()
    }

    override suspend fun onEnableAsync() {
        ListenerManager.registerListeners()

        mechanicRegistryImpl.enableMechanics()
        mechanicRegistryImpl.registerBukkitHandlers()

        licenseServiceImpl.onEnable()

        commandAPICommand("active-identity") {
            playerExecutor { player, args ->
                plugin.launch {
                    val rpPlayer = player.rpPlayer()
                    val activeIdentity = rpPlayer.activeIdentity

                    player.sendText {
                        appendPrefix()

                        info("Deine aktive Identität: ")
                        if (activeIdentity != null) {
                            append(activeIdentity.type)
                        } else {
                            variableValue("Keine aktive Identität gesetzt")
                        }
                    }
                }
            }
        }
    }

    override suspend fun onDisableAsync() {
        licenseServiceImpl.onDisable()
        mechanicRegistryImpl.disableMechanics()

        rpDatabase.onDisable()
    }

}
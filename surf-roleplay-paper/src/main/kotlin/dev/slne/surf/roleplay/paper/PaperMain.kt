package dev.slne.surf.roleplay.paper

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.roleplay.paper.command.activeIdentityCommand
import dev.slne.surf.roleplay.paper.command.balanceCommand
import dev.slne.surf.roleplay.paper.listener.ListenerManager
import org.bukkit.plugin.java.JavaPlugin

class PaperMain : SuspendingJavaPlugin() {

    override suspend fun onLoadAsync() {
        balanceCommand()
        activeIdentityCommand()
    }

    override suspend fun onEnableAsync() {
        ListenerManager.registerListeners()
    }

    override suspend fun onDisableAsync() {

    }

}

val plugin get() = JavaPlugin.getPlugin(PaperMain::class.java)
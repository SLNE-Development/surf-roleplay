package dev.slne.roleplay.mod.server

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.roleplay.mod.server.listener.ListenerManager
import org.bukkit.plugin.java.JavaPlugin

val plugin get() = JavaPlugin.getPlugin(ModServer::class.java)

class ModServer : SuspendingJavaPlugin() {
    override suspend fun onLoadAsync() {

    }

    override suspend fun onEnableAsync() {
        ListenerManager.register()
    }

    override suspend fun onDisableAsync() {

    }
}
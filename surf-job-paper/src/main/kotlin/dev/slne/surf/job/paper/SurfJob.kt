package dev.slne.surf.job.paper

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.job.paper.listener.ListenerManager
import dev.slne.surf.job.paper.utils.PermissionRegistry
import org.bukkit.plugin.java.JavaPlugin

val plugin get() = JavaPlugin.getPlugin(SurfJob::class.java)

class SurfJob : SuspendingJavaPlugin() {

    override suspend fun onLoadAsync() {

    }

    override suspend fun onEnableAsync() {
        PermissionRegistry.createJobJoinPermissions()
        ListenerManager.register()
    }

    override suspend fun onDisableAsync() {

    }

}
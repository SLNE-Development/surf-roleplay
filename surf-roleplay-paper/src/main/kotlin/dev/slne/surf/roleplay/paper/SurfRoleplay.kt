package dev.slne.surf.roleplay.paper

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.job.paper.SurfJob
import dev.slne.surf.roleplay.core.RpDatabase
import dev.slne.surf.roleplay.mechanic.mechanicRegistryImpl
import dev.slne.surf.roleplay.paper.listeners.ListenerManager
import org.bukkit.plugin.java.JavaPlugin

val plugin get() = JavaPlugin.getPlugin(SurfRoleplay::class.java)

class SurfRoleplay : SuspendingJavaPlugin() {

    private lateinit var rpDatabase: RpDatabase

    override suspend fun onLoadAsync() {
        rpDatabase = RpDatabase(dataPath)

        mechanicRegistryImpl.registerMechanics()
        mechanicRegistryImpl.registerDatabaseTables(rpDatabase)

        rpDatabase.onLoad()

        mechanicRegistryImpl.loadMechanics(this)

        SurfJob.onLoad(this)
    }

    override suspend fun onEnableAsync() {
        ListenerManager.registerListeners()

        mechanicRegistryImpl.enableMechanics()
        mechanicRegistryImpl.registerBukkitHandlers()

        SurfJob.onEnable()
    }

    override suspend fun onDisableAsync() {
        mechanicRegistryImpl.disableMechanics()

        SurfJob.onDisable()
        rpDatabase.onDisable()
    }

}
package dev.slne.surf.roleplay.paper

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.roleplay.core.RpDatabase
import dev.slne.surf.roleplay.mechanic.MechanicRegistry
import org.bukkit.plugin.java.JavaPlugin

val plugin get() = JavaPlugin.getPlugin(SurfRoleplay::class.java)

class SurfRoleplay : SuspendingJavaPlugin() {

    private lateinit var rpDatabase: RpDatabase

    override suspend fun onLoadAsync() {
        rpDatabase = RpDatabase(dataPath)
        rpDatabase.onLoad()

        MechanicRegistry.registerMechanics()
        MechanicRegistry.loadMechanics()
    }

    override suspend fun onEnableAsync() {
        MechanicRegistry.enableMechanics()
        MechanicRegistry.registerBukkitHandlers()
    }

    override suspend fun onDisableAsync() {
        MechanicRegistry.disableMechanics()

        rpDatabase.onDisable()
    }

}
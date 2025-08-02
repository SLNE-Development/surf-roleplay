package dev.slne.surf.roleplay.mechanic

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import com.google.auto.service.AutoService
import dev.slne.surf.roleplay.api.mechanic.Mechanic
import dev.slne.surf.roleplay.api.mechanic.MechanicRegistry
import dev.slne.surf.roleplay.core.RpDatabase
import dev.slne.surf.roleplay.mechanic.mechanics.idcard.IdCardMechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanics.license.LicenseMechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanics.test.TestMechanicImpl
import dev.slne.surf.surfapi.bukkit.api.event.register
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import net.kyori.adventure.util.Services

val plugin get() = mechanicRegistryImpl.plugin

@AutoService(MechanicRegistry::class)
class MechanicRegistryImpl : MechanicRegistry, Services.Fallback {

    lateinit var plugin: SuspendingJavaPlugin
        private set

    private val mechanics = mutableObjectSetOf<MechanicImpl>()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Mechanic> getMechanic(clazz: Class<T>) =
        mechanics.firstOrNull { it::class.java == clazz } as? T
            ?: throw IllegalArgumentException("No mechanic of class ${clazz.name} is registered.")

    fun registerMechanics() {
        mechanics.add(LicenseMechanicImpl)
        mechanics.add(TestMechanicImpl)
        mechanics.add(IdCardMechanicImpl)
    }

    fun registerBukkitHandlers() {
        mechanics.forEach { mechanic -> mechanic.handlers.forEach { it.register() } }
    }

    fun registerDatabaseTables(rpDatabase: RpDatabase) {
        mechanics.flatMap { it.getDatabaseTables() }
            .forEach { rpDatabase.registerMechanicTable(it) }
    }

    fun loadMechanics(plugin: SuspendingJavaPlugin) {
        this.plugin = plugin

        mechanics.forEach { it.onLoad(plugin) }
    }

    fun enableMechanics() {
        mechanics.forEach { it.onEnable(plugin) }
    }

    fun disableMechanics() {
        mechanics.forEach { it.onDisable(plugin) }
    }
}

val mechanicRegistryImpl get() = MechanicRegistry.INSTANCE as MechanicRegistryImpl
package dev.slne.surf.roleplay.mechanic

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import com.google.auto.service.AutoService
import dev.slne.surf.roleplay.api.mechanic.Mechanic
import dev.slne.surf.roleplay.api.mechanic.MechanicRegistry
import dev.slne.surf.roleplay.core.RpDatabase
import dev.slne.surf.roleplay.mechanic.mechanics.atm.AtmMechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanics.cash.CashMechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanics.idcard.IdCardMechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanics.jobwages.JobWagesMechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanics.license.LicenseMechanicImpl
import dev.slne.surf.surfapi.bukkit.api.event.register
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import net.kyori.adventure.util.Services

val plugin get() = mechanicRegistryImpl.plugin

@AutoService(MechanicRegistry::class)
class MechanicRegistryImpl : MechanicRegistry, Services.Fallback {

    lateinit var plugin: SuspendingJavaPlugin
        private set

    private val mechanics = mutableObjectSetOf<Mechanic>()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Mechanic> getMechanic(clazz: Class<T>) =
        mechanics.firstOrNull { clazz.isAssignableFrom(it.javaClass) } as? T
            ?: throw IllegalArgumentException("No mechanic of class ${clazz.name} is registered.")

    fun registerMechanics(plugin: SuspendingJavaPlugin) {
        this.plugin = plugin

        mechanics.add(LicenseMechanicImpl)
        mechanics.add(IdCardMechanicImpl)
        mechanics.add(AtmMechanicImpl)
        mechanics.add(JobWagesMechanicImpl)
        mechanics.add(CashMechanicImpl)
    }

    fun registerBukkitHandlers() {
        mechanics.forEach { mechanic -> mechanic.handlers.forEach { it.register() } }
    }

    fun registerDatabaseTables(rpDatabase: RpDatabase) {
        mechanics.map { it as MechanicImpl }.flatMap { it.getDatabaseTables() }
            .forEach { rpDatabase.registerMechanicTable(it) }
    }

    suspend fun loadMechanics() {
        mechanics.forEach { it.onLoad() }
    }

    suspend fun enableMechanics() {
        mechanics.forEach { it.onEnable() }
    }

    suspend fun disableMechanics() {
        mechanics.forEach { it.onDisable() }
    }
}

val mechanicRegistryImpl get() = MechanicRegistry.INSTANCE as MechanicRegistryImpl
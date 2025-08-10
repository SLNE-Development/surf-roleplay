package dev.slne.surf.roleplay.core.common.mechanics

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import com.google.auto.service.AutoService
import dev.slne.surf.roleplay.api.common.mechanic.Mechanic
import dev.slne.surf.roleplay.api.common.mechanic.MechanicRegistry
import dev.slne.surf.roleplay.core.common.mechanics.idcard.IdCardMechanicImpl
import dev.slne.surf.roleplay.core.common.mechanics.rentable.RentableMechanicImpl
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

        mechanics.add(IdCardMechanicImpl)
        mechanics.add(RentableMechanicImpl)
    }

    fun registerBukkitHandlers() {
        mechanics.forEach { mechanic -> mechanic.handlers.forEach { it.register() } }
    }

    fun startAllRpJobs() {
        mechanics.forEach { it.rpJobs.forEach { job -> job.start() } }
    }

    suspend fun stopAllRpJobs() {
        mechanics.forEach { it.rpJobs.forEach { job -> job.stop() } }
    }

    suspend fun loadMechanics() {
        mechanics.forEach { it.onLoad() }
    }

    suspend fun enableMechanics() {
        mechanics.forEach { it.onEnable() }

        startAllRpJobs()
    }

    suspend fun disableMechanics() {
        stopAllRpJobs()

        mechanics.forEach { it.onDisable() }
    }
}

val mechanicRegistryImpl get() = MechanicRegistry.INSTANCE as MechanicRegistryImpl
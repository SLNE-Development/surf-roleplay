package dev.slne.surf.roleplay.mechanic

import dev.slne.surf.roleplay.mechanic.mechanics.license.LicenseMechanic
import dev.slne.surf.roleplay.mechanic.mechanics.test.TestMechanic
import dev.slne.surf.surfapi.bukkit.api.event.register
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf

object MechanicRegistry {

    private val mechanics = mutableObjectSetOf<Mechanic>()

    fun registerMechanics() {
        mechanics.add(LicenseMechanic)
        mechanics.add(TestMechanic)
    }

    fun registerBukkitHandlers() {
        mechanics.forEach { mechanic -> mechanic.handlers.forEach { it.register() } }
    }

    fun loadMechanics() {
        mechanics.forEach { it.onLoad() }
    }

    fun enableMechanics() {
        mechanics.forEach { it.onEnable() }
    }

    fun disableMechanics() {
        mechanics.forEach { it.onDisable() }
    }

}
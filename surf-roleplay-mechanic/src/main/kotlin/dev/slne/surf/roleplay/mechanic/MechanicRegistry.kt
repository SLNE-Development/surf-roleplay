package dev.slne.surf.roleplay.mechanic

import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf

object MechanicRegistry {

    private val mechanics = mutableObjectSetOf<Mechanic>()

    fun registerMechanics() {

    }

    fun registerBukkitHandlers() {
        mechanics.forEach { it.registerBukkitHandlers() }
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
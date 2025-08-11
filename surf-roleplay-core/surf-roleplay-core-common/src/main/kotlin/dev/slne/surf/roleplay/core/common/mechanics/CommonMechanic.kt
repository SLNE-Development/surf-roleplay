package dev.slne.surf.roleplay.core.common.mechanics

import dev.slne.surf.roleplay.api.common.mechanic.Mechanic

abstract class CommonMechanic(
    override val name: String,
) : Mechanic {
    override suspend fun onLoad() {}
    override suspend fun onEnable() {}
    override suspend fun onDisable() {}
}
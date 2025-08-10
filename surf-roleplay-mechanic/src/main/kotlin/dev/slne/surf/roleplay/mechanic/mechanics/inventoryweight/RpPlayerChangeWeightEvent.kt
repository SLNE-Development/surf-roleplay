package dev.slne.surf.roleplay.mechanic.mechanics.inventoryweight

import dev.slne.surf.roleplay.api.events.RpEvent
import dev.slne.surf.roleplay.api.player.RpPlayer

class RpPlayerChangeWeightEvent(
    val player: RpPlayer,
    val from: Double,
    val to: Double
): RpEvent()
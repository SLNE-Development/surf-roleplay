package dev.slne.surf.roleplay.api.mechanic.jobwages.event

import dev.slne.surf.roleplay.api.events.CancellableRpEvent
import dev.slne.surf.roleplay.api.player.RpPlayer

class PlayerPaycheckEvent(
    val player: RpPlayer,
    var amount: Int,
) : CancellableRpEvent()
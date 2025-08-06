package dev.slne.surf.roleplay.api.mechanic.atm.event

import dev.slne.surf.roleplay.api.events.CancellableRpEvent
import dev.slne.surf.roleplay.api.player.RpPlayer

class PlayerDepositMoneyEvent(
    val player: RpPlayer,
    var amount: Int
) : CancellableRpEvent()
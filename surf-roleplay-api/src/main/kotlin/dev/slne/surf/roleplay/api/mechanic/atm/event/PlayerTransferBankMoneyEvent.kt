package dev.slne.surf.roleplay.api.mechanic.atm.event

import dev.slne.surf.roleplay.api.events.CancellableRpEvent
import dev.slne.surf.roleplay.api.player.RpPlayer

class PlayerTransferBankMoneyEvent(
    val player: RpPlayer,
    val receiver: RpPlayer,
    var amount: Int
) : CancellableRpEvent()
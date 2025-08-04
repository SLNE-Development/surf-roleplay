package dev.slne.surf.roleplay.api.mechanic.rentable.events

import dev.slne.surf.roleplay.api.events.CancellableRpEvent
import dev.slne.surf.roleplay.api.mechanic.rentable.Rentable

class RentableRentCollectEvent(
    val rentable: Rentable,
    var amount: Int,
) : CancellableRpEvent()
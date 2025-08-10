package dev.slne.surf.roleplay.api.paper.rentable.events

import dev.slne.surf.roleplay.api.common.mechanic.rentable.Rentable
import dev.slne.surf.roleplay.api.paper.events.CancellableRpEvent

/**
 * Event triggered when rent is collected from a rentable property.
 * This event is cancellable, allowing plugins to prevent the rent collection.
 *
 * @property rentable The rentable property from which the rent is being collected.
 * @property amount The amount of rent being collected.
 */
class RentableRentCollectEvent(
    val rentable: Rentable,
    var amount: Int,
) : CancellableRpEvent()
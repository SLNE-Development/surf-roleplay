package dev.slne.surf.roleplay.api.mechanic.rentable.events

import dev.slne.surf.roleplay.api.events.CancellableRpEvent
import dev.slne.surf.roleplay.api.mechanic.rentable.Rentable

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
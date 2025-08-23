package dev.slne.surf.roleplay.paper.mechanics.rentable.events

import dev.slne.surf.cloud.api.common.event.CancellableCloudEvent
import dev.slne.surf.roleplay.paper.mechanics.rentable.Rentable

/**
 * Event triggered when rent is collected from a rentable property.
 * This event is cancellable, allowing plugins to prevent the rent collection.
 *
 * @property rentable The rentable property from which the rent is being collected.
 * @property amount The amount of rent being collected.
 */
class RentableRentCollectEvent(
    source: Any,
    val rentable: Rentable,
    var amount: Int,
) : CancellableCloudEvent(source)
package dev.slne.surf.roleplay.api.paper.rentable.events

import dev.slne.surf.roleplay.api.common.mechanic.rentable.Rentable
import dev.slne.surf.roleplay.api.common.mechanic.rentable.RentableMechanic
import dev.slne.surf.roleplay.api.common.player.RpPlayer
import dev.slne.surf.roleplay.api.paper.events.CancellableRpEvent
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder

/**
 * Event triggered when the owner of a rentable property changes.
 * This event is cancellable, allowing plugins to prevent the owner change.
 *
 * @property rentable The rentable property whose owner is changing.
 * @property oldOwner The previous owner of the rentable property, or null if there was no previous owner.
 * @property newOwner The new owner of the rentable property, or null if the ownership is being removed.
 * @property reason The reason for the owner change.
 */
class RentableOwnerChangeEvent(
    val rentable: Rentable,
    val oldOwner: RpPlayer?,
    val newOwner: RpPlayer?,
    val reason: OwnerChangeReason,
) : CancellableRpEvent() {

    /**
     * The reason why the owner change was cancelled, if applicable.
     */
    var cancelReason: String? = null

    /**
     * The result of the owner change operation.
     */
    sealed class OwnerChangeReason {
        /**
         * The owner quit the game, resulting in the owner change.
         */
        data object OwnerQuit : OwnerChangeReason()

        /**
         * The owner of the rentable property was removed, resulting in the owner change.
         */
        data object RentableBought : OwnerChangeReason()

        /**
         * The owner of the rentable property was changed to a new owner.
         */
        data object OwnerSetNewOwner : OwnerChangeReason()
    }

    /**
     * The result of setting a new owner for the rentable property.
     */
    sealed class OwnerSetResult {
        /**
         * Indicates a successful owner change operation.
         */
        data object Success : OwnerSetResult()

        /**
         * Indicates a failure in setting the new owner for the rentable property.
         * Contains the reason for the failure.
         *
         * @property reason The reason why the owner change failed.
         */
        data class Failure(val reason: OwnerSetFailureReason) : OwnerSetResult()
    }

    /**
     * Represents the reasons why setting a new owner for a rentable property might fail.
     *
     * @property message A function that builds a message describing the failure reason.
     */
    sealed class OwnerSetFailureReason(val message: SurfComponentBuilder.() -> Unit) {

        /**
         * Indicates that the new owner is already a member of the rentable property.
         */
        data object AlreadyOwned : OwnerSetFailureReason({
            error("Dieses Mietobjekt wird bereits von jemand anderem besessen.")
        })

        /**
         * Indicates that the new owner already has too many rentable properties.
         *
         * @param ownedRentables The number of rentable properties the owner currently has.
         * @param maxOwnableRentables The maximum number of rentable properties the owner can have.
         */
        data class AlreadyOwningTooManyRentables(
            val ownedRentables: Int,
            val maxOwnableRentables: Int
        ) : OwnerSetFailureReason({
            error("Du besitzt bereits zu viele Mitobjekte ")
            variableValue(ownedRentables)
            error("/")
            variableValue(maxOwnableRentables)
            error(" und kannst kein weiteres besitzen.")
        })

        /**
         * Indicates that the new owner does not have enough money to buy the rentable property.
         *
         * @param currentMoney The amount of money the new owner currently has.
         * @param requiredMoney The amount of money required to buy the rentable property.
         */
        data class NotEnoughMoney(
            val currentMoney: Int,
            val requiredMoney: Int
        ) : OwnerSetFailureReason({
            error("Du hast nicht genug Geld, um dieses Mietobjekt zu kaufen. ")
            variableValue("$currentMoney €")
            error("/")
            variableValue("$requiredMoney €")
            error(" benötigt.")
        })

        /**
         * Indicates that the rent collection failed for the rentable property.
         *
         * @param reason The reason for the rent collection failure.
         */
        data class RentCollectionFailed(
            val reason: RentableMechanic.RentCollectResult
        ) : OwnerSetFailureReason({
            error("Die Miete konnte nicht eingezogen werden: ")
            variableValue(reason.name)
        })

        /**
         * Indicates that the event was cancelled, preventing the owner change.
         *
         * @param reason The reason why the event was cancelled.
         */
        data class EventCancelled(val reason: String) : OwnerSetFailureReason({
            error("Das Event wurde abgebrochen: ")
            variableValue(reason)
        })
    }
}
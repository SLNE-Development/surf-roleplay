package dev.slne.surf.roleplay.api.paper.rentable.events

import dev.slne.surf.roleplay.api.common.mechanic.rentable.Rentable
import dev.slne.surf.roleplay.api.common.player.RpPlayer
import dev.slne.surf.roleplay.api.paper.events.CancellableRpEvent

/**
 * Event triggered when a member is removed from a rentable property.
 * This event is cancellable, allowing plugins to prevent the removal of a member.
 *
 * @property rentable The rentable property from which the member is being removed.
 * @property member The player who is being removed as a member from the rentable property.
 */
class RentableMemberRemoveEvent(
    val rentable: Rentable,
    val member: RpPlayer,
) : CancellableRpEvent() {

    /**
     * The result of the member removal operation.
     */
    sealed class MemberRemoveResult {
        /**
         * Indicates a successful removal of the member from the rentable property.
         */
        data object Success : MemberRemoveResult()

        /**
         * Indicates a failure in removing the member from the rentable property.
         * Contains the reason for the failure.
         *
         * @property reason The reason why the removal failed.
         */
        data class Failure(val reason: MemberRemoveFailureReason) : MemberRemoveResult()
    }

    /**
     * Represents the reasons why removing a member from a rentable property might fail.
     */
    sealed class MemberRemoveFailureReason {
        /**
         * Indicates that the member is not part of the rentable property.
         */
        data object NotMember : MemberRemoveFailureReason()

        /**
         * Indicates that the event was cancelled, preventing the member from being removed.
         */
        data object EventCancelled : MemberRemoveFailureReason()
    }
}
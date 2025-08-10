package dev.slne.surf.roleplay.api.paper.rentable.events

import dev.slne.surf.roleplay.api.common.mechanic.rentable.Rentable
import dev.slne.surf.roleplay.api.common.player.RpPlayer
import dev.slne.surf.roleplay.api.paper.events.CancellableRpEvent

/**
 * Event triggered when a member is added to a rentable property.
 * This event is cancellable, allowing plugins to prevent the addition of a member.
 *
 * @property rentable The rentable property to which the member is being added.
 * @property member The player who is being added as a member to the rentable property.
 */
class RentableMemberAddEvent(
    val rentable: Rentable,
    val member: RpPlayer,
) : CancellableRpEvent() {

    /**
     * The result of the member addition operation.
     */
    sealed class MemberAddResult {
        /**
         * Indicates a successful addition of the member to the rentable property.
         */
        data object Success : MemberAddResult()

        /**
         * Indicates a failure in adding the member to the rentable property.
         * Contains the reason for the failure.
         *
         * @property reason The reason why the addition failed.
         */
        data class Failure(val reason: MemberAddFailureReason) : MemberAddResult()
    }

    /**
     * Represents the reasons why adding a member to a rentable property might fail.
     */
    sealed class MemberAddFailureReason {
        /**
         * Indicates that the member is already part of the rentable property.
         */
        data object AlreadyMember : MemberAddFailureReason()

        /**
         * Indicates that the addition of the member was cancelled by an event handler.
         */
        data object EventCancelled : MemberAddFailureReason()
    }
}
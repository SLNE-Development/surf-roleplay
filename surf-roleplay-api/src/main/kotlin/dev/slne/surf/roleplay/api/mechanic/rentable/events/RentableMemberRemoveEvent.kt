package dev.slne.surf.roleplay.api.mechanic.rentable.events

import dev.slne.surf.roleplay.api.events.CancellableRpEvent
import dev.slne.surf.roleplay.api.mechanic.rentable.Rentable
import dev.slne.surf.roleplay.api.player.RpPlayer

class RentableMemberRemoveEvent(
    val rentable: Rentable,
    val member: RpPlayer,
) : CancellableRpEvent() {

    sealed class MemberRemoveResult {
        data object Success : MemberRemoveResult()
        data class Failure(val reason: MemberRemoveFailureReason) : MemberRemoveResult()
    }

    sealed class MemberRemoveFailureReason {
        data object NotMember : MemberRemoveFailureReason()
        data object EventCancelled : MemberRemoveFailureReason()
    }
}
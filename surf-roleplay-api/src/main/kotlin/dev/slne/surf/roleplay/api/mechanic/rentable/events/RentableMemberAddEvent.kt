package dev.slne.surf.roleplay.api.mechanic.rentable.events

import dev.slne.surf.roleplay.api.events.CancellableRpEvent
import dev.slne.surf.roleplay.api.mechanic.rentable.Rentable
import dev.slne.surf.roleplay.api.player.RpPlayer

class RentableMemberAddEvent(
    val rentable: Rentable,
    val member: RpPlayer,
) : CancellableRpEvent() {

    sealed class MemberAddResult {
        data object Success : MemberAddResult()
        data class Failure(val reason: MemberAddFailureReason) : MemberAddResult()
    }

    sealed class MemberAddFailureReason {
        data object AlreadyMember : MemberAddFailureReason()
        data object EventCancelled : MemberAddFailureReason()
    }
}
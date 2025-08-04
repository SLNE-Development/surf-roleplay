package dev.slne.surf.roleplay.api.mechanic.rentable.events

import dev.slne.surf.roleplay.api.events.CancellableRpEvent
import dev.slne.surf.roleplay.api.mechanic.rentable.Rentable
import dev.slne.surf.roleplay.api.mechanic.rentable.RentableMechanic
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder

class RentableOwnerChangeEvent(
    val rentable: Rentable,
    val oldOwner: RpPlayer?,
    val newOwner: RpPlayer?,
    val reason: OwnerChangeReason,
) : CancellableRpEvent() {

    var cancelReason: String? = null

    sealed class OwnerChangeReason {
        data object PlayerQuit : OwnerChangeReason()
        data object RentableBought : OwnerChangeReason()
        data object OwnerSetNewOwner : OwnerChangeReason()
    }

    sealed class OwnerSetResult {
        data object Success : OwnerSetResult()
        data class Failure(val reason: OwnerSetFailureReason) : OwnerSetResult()
    }

    sealed class OwnerSetFailureReason(val message: SurfComponentBuilder.() -> Unit) {
        data object AlreadyOwned : OwnerSetFailureReason({
            error("Dieses Mietobjekt wird bereits von jemand anderem besessen.")
        })

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

        data class RentCollectionFailed(
            val reason: RentableMechanic.RentCollectResult
        ) : OwnerSetFailureReason({
            error("Die Miete konnte nicht eingezogen werden: ")
            variableValue(reason.name)
        })

        data class EventCancelled(val reason: String) : OwnerSetFailureReason({
            error("Das Event wurde abgebrochen: ")
            variableValue(reason)
        })
    }
}
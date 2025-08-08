package dev.slne.surf.roleplay.core.player.identity.identities

import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.identity.RpIdentity
import dev.slne.surf.roleplay.api.player.utils.BalanceType
import dev.slne.surf.roleplay.core.player.identity.RpIdentityImpl
import dev.slne.surf.surfapi.core.api.util.mutableObject2ObjectMapOf
import java.time.LocalDate
import java.time.ZonedDateTime

class RescueServiceIdentityImpl(
    player: RpPlayer,
    firstName: String,
    lastName: String,
    dateOfBirth: LocalDate,
    override val rank: String,
    createdAt: ZonedDateTime = ZonedDateTime.now(),
    updatedAt: ZonedDateTime = ZonedDateTime.now()
) : RpIdentityImpl(
    player = player,
    type = RpIdentity.RpIdentityType.RESCUE_SERVICE,
    firstName,
    lastName,
    dateOfBirth,
    createdAt,
    updatedAt
), RpIdentity.RescueServiceIdentity {
    override fun getCurrencyNames() = mutableObject2ObjectMapOf(
        BalanceType.CASH to "rescue_service_cash",
        BalanceType.BANK to "rescue_service_bank",
        BalanceType.CRYPTO to "rescue_service_crypto",
    )

    override fun toString(): String {
        val parent = super.toString()

        return "RescueServiceIdentityImpl(rank='$rank', $parent)"
    }
}
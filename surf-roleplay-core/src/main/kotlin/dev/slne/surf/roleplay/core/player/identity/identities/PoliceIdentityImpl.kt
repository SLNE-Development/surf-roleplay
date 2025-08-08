package dev.slne.surf.roleplay.core.player.identity.identities

import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.identity.RpIdentity
import dev.slne.surf.roleplay.api.player.utils.BalanceType
import dev.slne.surf.roleplay.core.player.identity.RpIdentityImpl
import dev.slne.surf.surfapi.core.api.util.mutableObject2ObjectMapOf
import java.time.LocalDate
import java.time.ZonedDateTime

class PoliceIdentityImpl(
    player: RpPlayer,
    firstName: String,
    lastName: String,
    dateOfBirth: LocalDate,
    override val badgeNumber: String,
    override val rank: String,
    createdAt: ZonedDateTime = ZonedDateTime.now(),
    updatedAt: ZonedDateTime = ZonedDateTime.now()
) : RpIdentityImpl(
    player = player,
    type = RpIdentity.RpIdentityType.POLICE,
    firstName,
    lastName,
    dateOfBirth,
    createdAt,
    updatedAt
), RpIdentity.PoliceIdentity {
    override fun getCurrencyNames() = mutableObject2ObjectMapOf(
        BalanceType.CASH to "police_cash",
        BalanceType.BANK to "police_bank",
        BalanceType.CRYPTO to "police_crypto",
    )
}
package dev.slne.surf.roleplay.core.player.identity.identities

import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.identity.RpIdentity
import dev.slne.surf.roleplay.api.player.utils.BalanceType
import dev.slne.surf.roleplay.core.player.identity.RpIdentityImpl
import dev.slne.surf.surfapi.core.api.util.mutableObject2ObjectMapOf
import java.time.LocalDate
import java.time.ZonedDateTime

class CivilianIdentityImpl(
    player: RpPlayer,
    firstName: String,
    lastName: String,
    dateOfBirth: LocalDate,
    createdAt: ZonedDateTime = ZonedDateTime.now(),
    updatedAt: ZonedDateTime = ZonedDateTime.now()
) : RpIdentityImpl(
    player,
    type = RpIdentity.RpIdentityType.CIVILIAN,
    firstName,
    lastName,
    dateOfBirth,
    createdAt,
    updatedAt
), RpIdentity.CivilianIdentity {
    override fun toString(): String {
        val parent = super.toString()

        return "CivilianIdentityImpl($parent)"
    }
}
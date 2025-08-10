package dev.slne.surf.roleplay.core.common.player.identity.identities

import dev.slne.surf.roleplay.api.common.player.RpPlayer
import dev.slne.surf.roleplay.api.common.player.identity.RpIdentity
import dev.slne.surf.roleplay.core.common.player.license.CommonRpIdentity
import java.time.LocalDate
import java.time.ZonedDateTime

class CivilianIdentityImpl(
    player: RpPlayer,
    firstName: String,
    lastName: String,
    dateOfBirth: LocalDate,
    createdAt: ZonedDateTime = ZonedDateTime.now(),
    updatedAt: ZonedDateTime = ZonedDateTime.now()
) : CommonRpIdentity(
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
package dev.slne.surf.roleplay.core.common.player.identity.identities

import dev.slne.surf.roleplay.api.common.player.RpPlayer
import dev.slne.surf.roleplay.api.common.player.identity.RpIdentity
import dev.slne.surf.roleplay.core.common.player.license.CommonRpIdentity
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
) : CommonRpIdentity(
    player = player,
    type = RpIdentity.RpIdentityType.POLICE,
    firstName,
    lastName,
    dateOfBirth,
    createdAt,
    updatedAt
), RpIdentity.PoliceIdentity {
    override fun toString(): String {
        val parent = super.toString()

        return "PoliceIdentityImpl(badgeNumber='$badgeNumber', rank='$rank', $parent)"
    }
}
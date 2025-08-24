package dev.slne.surf.roleplay.paper.player.identity.identities

import dev.slne.surf.roleplay.core.common.player.identity.NetworkIdentity
import dev.slne.surf.roleplay.core.common.player.identity.RpIdentityType
import dev.slne.surf.roleplay.paper.player.identity.RpIdentity
import kotlinx.serialization.Contextual
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.*

class PoliceIdentity(
    override val uuid: @Contextual UUID,
    override var firstName: String,
    override var lastName: String,
    override var dateOfBirth: @Contextual LocalDate,
    val badgeNumber: String,
    val rank: String,
    override val createdAt: @Contextual ZonedDateTime = ZonedDateTime.now(),
    override var updatedAt: @Contextual ZonedDateTime = ZonedDateTime.now()
) : RpIdentity() {
    override val type = RpIdentityType.POLICE

    override fun toNetwork() = NetworkIdentity.Police(
        uuid = uuid,
        firstName = firstName,
        lastName = lastName,
        dateOfBirth = dateOfBirth,
        badgeNumber = badgeNumber,
        rank = rank,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    override fun toString(): String {
        return "PoliceIdentity(rank='$rank') ${super.toString()}"
    }
}
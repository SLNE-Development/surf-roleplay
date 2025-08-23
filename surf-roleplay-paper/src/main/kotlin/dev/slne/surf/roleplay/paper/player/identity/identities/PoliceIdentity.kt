package dev.slne.surf.roleplay.paper.player.identity.identities

import dev.slne.surf.roleplay.paper.player.identity.RpIdentity
import dev.slne.surf.roleplay.core.common.player.identity.RpIdentityType
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.*

@Serializable
class PoliceIdentity(
    override val uuid: @Contextual UUID,
    override var firstName: String,
    override var lastName: String,
    override var dateOfBirth: @Contextual LocalDate,
    val badgeNumber: String,
    val rank: String,
    override val createdAt: @Contextual ZonedDateTime = ZonedDateTime.now(),
    override var updatedAt: @Contextual ZonedDateTime = ZonedDateTime.now()
) : RpIdentity(
    uuid,
    RpIdentityType.POLICE,
    firstName,
    lastName,
    dateOfBirth,
    createdAt,
    updatedAt
) {
    override fun toString(): String {
        val parent = super.toString()

        return "PoliceIdentityImpl(badgeNumber='$badgeNumber', rank='$rank', $parent)"
    }
}
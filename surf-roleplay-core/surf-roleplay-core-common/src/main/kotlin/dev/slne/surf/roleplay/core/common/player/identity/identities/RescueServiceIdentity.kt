package dev.slne.surf.roleplay.core.common.player.identity.identities

import dev.slne.surf.roleplay.core.common.player.identity.RpIdentity
import dev.slne.surf.roleplay.core.common.player.identity.RpIdentityType
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.*

@Serializable
class RescueServiceIdentity(
    override val uuid: @Contextual UUID,
    override var firstName: String,
    override var lastName: String,
    override var dateOfBirth: @Contextual LocalDate,
    val rank: String,
    override val createdAt: @Contextual ZonedDateTime = ZonedDateTime.now(),
    override var updatedAt: @Contextual ZonedDateTime = ZonedDateTime.now()
) : RpIdentity(
    uuid,
    RpIdentityType.RESCUE_SERVICE,
    firstName,
    lastName,
    dateOfBirth,
    createdAt,
    updatedAt
) {
    override fun toString(): String {
        val parent = super.toString()

        return "RescueServiceIdentityImpl(rank='$rank', $parent)"
    }
}
package dev.slne.surf.roleplay.paper.player.identity.identities

import dev.slne.surf.roleplay.core.common.player.identity.NetworkIdentity
import dev.slne.surf.roleplay.core.common.player.identity.RpIdentityType
import dev.slne.surf.roleplay.paper.player.identity.RpIdentity
import kotlinx.serialization.Contextual
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.*

class CivilianIdentity(
    override val uuid: @Contextual UUID,
    override var firstName: String,
    override var lastName: String,
    override var dateOfBirth: @Contextual LocalDate,
    override val createdAt: @Contextual ZonedDateTime = ZonedDateTime.now(),
    override var updatedAt: @Contextual ZonedDateTime = ZonedDateTime.now()
) : RpIdentity() {
    override val type = RpIdentityType.CIVILIAN

    override fun toNetwork() = NetworkIdentity.Civilian(
        uuid = uuid,
        firstName = firstName,
        lastName = lastName,
        dateOfBirth = dateOfBirth,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    override fun toString(): String {
        return "CivilianIdentity() ${super.toString()}"
    }
}
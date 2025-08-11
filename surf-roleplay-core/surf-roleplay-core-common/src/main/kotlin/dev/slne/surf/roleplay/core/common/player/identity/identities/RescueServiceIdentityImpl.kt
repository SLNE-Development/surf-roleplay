package dev.slne.surf.roleplay.core.common.player.identity.identities

import dev.slne.surf.roleplay.api.common.player.identity.RpIdentity
import dev.slne.surf.roleplay.core.common.player.identity.CommonRpIdentity
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.*

@Serializable
class RescueServiceIdentityImpl(
    override val uuid: @Contextual UUID,
    override var firstName: String,
    override var lastName: String,
    override var dateOfBirth: @Contextual LocalDate,
    override val rank: String,
    override val createdAt: @Contextual ZonedDateTime = ZonedDateTime.now(),
    override var updatedAt: @Contextual ZonedDateTime = ZonedDateTime.now()
) : CommonRpIdentity(RpIdentityCodecType.RESCUE_SERVICE), RpIdentity.RescueServiceIdentity {

    override val type = RpIdentity.RpIdentityType.RESCUE_SERVICE

    override fun toString(): String {
        val parent = super.toString()

        return "RescueServiceIdentityImpl(rank='$rank', $parent)"
    }
}
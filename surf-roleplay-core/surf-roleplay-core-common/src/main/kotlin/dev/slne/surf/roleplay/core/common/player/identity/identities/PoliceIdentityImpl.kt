package dev.slne.surf.roleplay.core.common.player.identity.identities

import dev.slne.surf.roleplay.api.common.player.identity.RpIdentity
import dev.slne.surf.roleplay.core.common.player.identity.CommonRpIdentity
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.*

@Serializable
class PoliceIdentityImpl(
    override val uuid: @Contextual UUID,
    override var firstName: String,
    override var lastName: String,
    override var dateOfBirth: @Contextual LocalDate,
    override val badgeNumber: String,
    override val rank: String,
    override val createdAt: @Contextual ZonedDateTime = ZonedDateTime.now(),
    override var updatedAt: @Contextual ZonedDateTime = ZonedDateTime.now()
) : CommonRpIdentity(RpIdentityCodecType.POLICE), RpIdentity.PoliceIdentity {

    override val type = RpIdentity.RpIdentityType.POLICE

    override fun toString(): String {
        val parent = super.toString()

        return "PoliceIdentityImpl(badgeNumber='$badgeNumber', rank='$rank', $parent)"
    }
}
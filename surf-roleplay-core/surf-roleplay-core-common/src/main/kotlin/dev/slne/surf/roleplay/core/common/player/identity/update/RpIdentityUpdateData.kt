package dev.slne.surf.roleplay.core.common.player.identity.update

import dev.slne.surf.roleplay.core.common.player.identity.RpIdentityType
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
sealed class RpIdentityUpdateData(val identityType: RpIdentityType) {
    abstract val firstName: String?
    abstract val lastName: String?
    abstract val dateOfBirth: LocalDate?

    @Serializable
    data class Civilian(
        override val firstName: String? = null,
        override val lastName: String? = null,
        override val dateOfBirth: @Contextual LocalDate? = null
    ) : RpIdentityUpdateData(RpIdentityType.CIVILIAN)

    @Serializable
    data class Police(
        override val firstName: String? = null,
        override val lastName: String? = null,
        override val dateOfBirth: @Contextual LocalDate? = null,
        val badgeNumber: String? = null,
        val rank: String? = null
    ) : RpIdentityUpdateData(RpIdentityType.POLICE)

    @Serializable
    data class RescueService(
        override val firstName: String? = null,
        override val lastName: String? = null,
        override val dateOfBirth: @Contextual LocalDate? = null,
        val rank: String? = null
    ) : RpIdentityUpdateData(RpIdentityType.RESCUE_SERVICE)

}
package dev.slne.surf.roleplay.core.common.player.identity.create

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
sealed class RpIdentityCreationData {
    abstract val firstName: String
    abstract val lastName: String
    abstract val dateOfBirth: LocalDate

    @Serializable
    data class Civilian(
        override val firstName: String,
        override val lastName: String,
        override val dateOfBirth: @Contextual LocalDate
    ) : RpIdentityCreationData()

    @Serializable
    data class Police(
        override val firstName: String,
        override val lastName: String,
        override val dateOfBirth: @Contextual LocalDate,
        val badgeNumber: String,
        val rank: String
    ) : RpIdentityCreationData()

    @Serializable
    data class RescueService(
        override val firstName: String,
        override val lastName: String,
        override val dateOfBirth: @Contextual LocalDate,
        val rank: String
    ) : RpIdentityCreationData()

}
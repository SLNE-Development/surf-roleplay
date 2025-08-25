package dev.slne.surf.roleplay.core.common.player.identity

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.*

@Serializable
sealed class NetworkIdentity(val type: RpIdentityType) {
    abstract val uuid: @Contextual UUID
    abstract val firstName: String
    abstract val lastName: String
    abstract val dateOfBirth: @Contextual LocalDate
    abstract val createdAt: @Contextual ZonedDateTime
    abstract val updatedAt: @Contextual ZonedDateTime

    @Serializable
    data class Civilian(
        override val uuid: @Contextual UUID,
        override val firstName: String,
        override val lastName: String,
        override val dateOfBirth: @Contextual LocalDate,
        override val createdAt: @Contextual ZonedDateTime = ZonedDateTime.now(),
        override val updatedAt: @Contextual ZonedDateTime = ZonedDateTime.now()
    ) : NetworkIdentity(RpIdentityType.CIVILIAN)

    @Serializable
    data class Police(
        override val uuid: @Contextual UUID,
        override val firstName: String,
        override val lastName: String,
        override val dateOfBirth: @Contextual LocalDate,
        val badgeNumber: String,
        val rank: String,
        override val createdAt: @Contextual ZonedDateTime = ZonedDateTime.now(),
        override val updatedAt: @Contextual ZonedDateTime = ZonedDateTime.now()
    ) : NetworkIdentity(RpIdentityType.POLICE)

    @Serializable
    data class RescueService(
        override val uuid: @Contextual UUID,
        override val firstName: String,
        override val lastName: String,
        override val dateOfBirth: @Contextual LocalDate,
        val rank: String,
        override val createdAt: @Contextual ZonedDateTime = ZonedDateTime.now(),
        override val updatedAt: @Contextual ZonedDateTime = ZonedDateTime.now()
    ) : NetworkIdentity(RpIdentityType.RESCUE_SERVICE)
}
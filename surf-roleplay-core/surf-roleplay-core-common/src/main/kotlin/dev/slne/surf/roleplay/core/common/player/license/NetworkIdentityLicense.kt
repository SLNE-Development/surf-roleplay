package dev.slne.surf.roleplay.core.common.player.license

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import net.kyori.adventure.key.Key
import java.time.ZonedDateTime

@Serializable
data class NetworkIdentityLicense(
    val licenseKey: @Contextual Key,
    val expiresAt: @Contextual ZonedDateTime?,
    val createdAt: @Contextual ZonedDateTime = ZonedDateTime.now()
) {
}
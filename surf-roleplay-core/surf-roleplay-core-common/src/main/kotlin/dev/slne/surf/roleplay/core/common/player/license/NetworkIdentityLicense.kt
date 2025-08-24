package dev.slne.surf.roleplay.core.common.player.license

import dev.slne.surf.roleplay.core.common.player.identity.RpIdentityType
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import net.kyori.adventure.key.Key
import java.time.ZonedDateTime
import java.util.*

@Serializable
data class NetworkIdentityLicense(
    val owner: @Contextual UUID,
    val identityType: RpIdentityType,
    val licenseKey: @Contextual Key,
    val expiresAt: @Contextual ZonedDateTime?,
    val createdAt: @Contextual ZonedDateTime = ZonedDateTime.now()
) {
}
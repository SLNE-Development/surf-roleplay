package dev.slne.surf.roleplay.api.mechanic.license

import dev.slne.surf.roleplay.api.player.RpPlayer
import java.time.ZonedDateTime

data class PlayerLicense(
    val player: RpPlayer,
    val license: License,
    val expiresAt: ZonedDateTime?,
    val createdAt: ZonedDateTime = ZonedDateTime.now()
) {
    val isExpired get() = expiresAt?.isBefore(ZonedDateTime.now()) ?: false

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PlayerLicense) return false

        if (player != other.player) return false
        if (license != other.license) return false

        return true
    }

    override fun hashCode(): Int {
        var result = player.hashCode()
        result = 31 * result + license.hashCode()
        return result
    }
}
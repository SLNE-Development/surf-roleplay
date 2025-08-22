package dev.slne.surf.roleplay.core.common.player.license

import dev.slne.surf.roleplay.core.common.player.identity.RpIdentity
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

/**
 * Represents a license associated with a player's identity in the roleplay system.
 *
 * This class encapsulates the details of a license, including the license type, the identity it is associated with,
 * the expiration time, and the creation time.
 *
 * @property license The [License] associated with this identity.
 * @property identity The [RpIdentity] associated with this license.
 * @property expiresAt The expiration time of the license, if applicable. Defaults to null if the license does not expire.
 */
@Serializable
data class IdentityLicense(
    val license: @Contextual License,
    val identity: RpIdentity,
    val expiresAt: @Contextual ZonedDateTime?,
    val createdAt: @Contextual ZonedDateTime = ZonedDateTime.now()
) {
    /**
     * Checks if the license is expired.
     */
    val isExpired get() = expiresAt?.isBefore(ZonedDateTime.now()) ?: false

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is IdentityLicense) return false

        if (license != other.license) return false
        if (identity != other.identity) return false

        return true
    }

    override fun hashCode(): Int {
        var result = license.hashCode()
        result = 31 * result + identity.hashCode()
        return result
    }

    override fun toString(): String {
        return "IdentityLicense(license=$license, expiresAt=$expiresAt, createdAt=$createdAt, isExpired=$isExpired)"
    }

    companion object {
        /**
         * Creates an [IdentityLicense] from a [License] and an [RpIdentity].
         *
         * @param license The [License] to create the identity license from.
         * @param identity The [RpIdentity] associated with the license.
         * @param createdAt The creation time of the identity license, defaults to the current time.
         * @return A new [IdentityLicense] instance.
         */
        fun createFromLicense(
            license: License,
            identity: RpIdentity,
            createdAt: ZonedDateTime = ZonedDateTime.now()
        ) = IdentityLicense(
            license = license,
            identity = identity,
            expiresAt = if (license is ExpirableLicense) {
                ZonedDateTime.now().plusSeconds(license.expiresIn.inWholeSeconds)
            } else {
                null
            },
            createdAt = createdAt
        )
    }

}
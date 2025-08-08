package dev.slne.surf.roleplay.api.player.license

import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.identity.RpIdentity
import dev.slne.surf.roleplay.api.player.license.utils.LicenseCreateResult
import dev.slne.surf.roleplay.api.player.license.utils.LicenseRemovedReason
import dev.slne.surf.roleplay.api.player.license.utils.UnobtainableReason
import it.unimi.dsi.fastutil.objects.ObjectSet
import org.jetbrains.annotations.Unmodifiable

interface HasLicenses {

    /**
     * The licenses owned by the player.
     */
    val licenses: @Unmodifiable ObjectSet<IdentityLicense>

    /**
     * Adds a license to the player.
     *
     * @param license The license to add.
     * @return A triple containing a boolean indicating success or failure, an [UnobtainableReason] if the license could not be obtained
     * and the [IdentityLicense] if successfully added.
     */
    suspend fun addLicense(license: License): LicenseCreateResult

    /**
     * Removes a license from the player.
     *
     * @param license The license to remove.
     * @param reason The reason for the removal.
     * @return A boolean indicating whether the removal was successful.
     */
    suspend fun removeLicense(license: License, reason: LicenseRemovedReason): Boolean

    /**
     * Confiscates the specified [license].
     * This method also removes all children licenses of the specified license.
     *
     * @param identity The identity of the player whose license is being confiscated.
     * @param license The license to be confiscated.
     * @param confiscatedBy The player who is confiscating the license.
     * @param confiscatedReason The reason for confiscating the license.
     * @return `true` if the license was successfully confiscated, `false` otherwise.
     */
    suspend fun confiscateLicense(
        identity: RpIdentity,
        license: License,
        confiscatedBy: RpPlayer,
        confiscatedReason: String
    ): Boolean

    /**
     * Retrieves a specific license owned by the player.
     *
     * @param license The class of the license to retrieve.
     * @return The [IdentityLicense] if found, or null if not found.
     */
    fun getLicense(license: Class<out License>): IdentityLicense?

    /**
     * Checks if the player has a specific license by its class type.
     *
     * @param license The class of the license to check.
     * @return A boolean indicating whether the player has the license.
     */
    fun hasLicense(license: Class<out License>) = getLicense(license) != null

    /**
     * Checks if the player has a specific license.
     *
     * @param license The license to check.
     * @return A boolean indicating whether the player has the license.
     */
    fun hasLicense(license: License) = hasLicense(license::class.java)
}

/**
 * Extension function to retrieve a license by its type.
 *
 * @param T The type of the license to retrieve.
 * @return The [IdentityLicense] if found, or null if not found.
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T : License> HasLicenses.getLicense() =
    getLicense(T::class.java)

/**
 * Extension function to check if the player has a specific license by its type.
 *
 * @param T The type of the license to check.
 * @return A boolean indicating whether the player has the license.
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T : License> HasLicenses.hasLicense() =
    hasLicense(T::class.java)
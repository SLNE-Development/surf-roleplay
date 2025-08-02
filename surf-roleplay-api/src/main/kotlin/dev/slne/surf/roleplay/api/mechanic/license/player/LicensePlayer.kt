package dev.slne.surf.roleplay.api.mechanic.license.player

import dev.slne.surf.roleplay.api.mechanic.license.License
import dev.slne.surf.roleplay.api.mechanic.license.PlayerLicense
import dev.slne.surf.roleplay.api.mechanic.license.utils.LicenseRemovedReason
import dev.slne.surf.roleplay.api.mechanic.license.utils.UnobtainableReason
import it.unimi.dsi.fastutil.objects.ObjectSet

interface LicensePlayer {

    /**
     * The licenses owned by the player.
     */
    val licenses: ObjectSet<PlayerLicense>

    /**
     * Adds a license to the player.
     *
     * @param license The license to add.
     * @return A pair containing a boolean indicating success or failure, and an [UnobtainableReason] if the license could not be obtained.
     */
    suspend fun addLicense(license: License): Pair<Boolean, UnobtainableReason?>

    /**
     * Removes a license from the player.
     *
     * @param license The license to remove.
     * @param reason The reason for the removal.
     * @return A boolean indicating whether the removal was successful.
     */
    suspend fun removeLicense(license: License, reason: LicenseRemovedReason): Boolean

    /**
     * Retrieves a specific license owned by the player.
     *
     * @param license The class of the license to retrieve.
     * @return The [PlayerLicense] if found, or null if not found.
     */
    fun getLicense(license: Class<out License>): PlayerLicense?

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
 * @return The [PlayerLicense] if found, or null if not found.
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T : License> LicensePlayer.getLicense() =
    getLicense(T::class.java)

/**
 * Extension function to check if the player has a specific license by its type.
 *
 * @param T The type of the license to check.
 * @return A boolean indicating whether the player has the license.
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T : License> LicensePlayer.hasLicense() =
    hasLicense(T::class.java)
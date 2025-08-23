@file:OptIn(InternalRoleplayApi::class)

package dev.slne.surf.roleplay.core.common.player.license

import dev.slne.surf.cloud.api.common.util.toObjectSet
import dev.slne.surf.roleplay.core.common.player.RpPlayer
import dev.slne.surf.roleplay.core.common.player.identity.RpIdentity
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.key.Key
import org.jetbrains.annotations.Unmodifiable
import org.springframework.beans.factory.getBean
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
abstract class LicenseService(
    private val context: ApplicationContext
) {

    /**
     * All licenses available in the game.
     */
    val licenses: @Unmodifiable ObjectSet<License>
        get() = context.getBeansOfType(License::class.java)
            .values
            .toObjectSet()

    /**
     * Retrieves a license of the specified type.
     *
     * @param T The type of the license to retrieve.
     * @param license The class object of the license type.
     * @return An instance of the requested license type.
     */
    fun <T : License> getLicense(license: Class<out T>): T = context.getBean(license)

    /**
     * Returns the license by its key.
     *
     * @param key the key of the license to retrieve.
     * @return the license instance if found, or null if not found.
     */
    fun getLicenseByKey(key: Key): License? = licenses.find { it.key == key }

    /**
     * Returns the license by its key.
     *
     * @param key the key of the license to retrieve.
     * @return the license instance if found.
     * @throws IllegalArgumentException if the license is not found.
     */
    fun getLicenseByKeyOrThrow(key: Key): License =
        getLicenseByKey(key) ?: error("License with key $key not found in the context.")

    /**
     * Creates a new license for the given identity.
     *
     * @param identityLicense the license to create.
     * @return the result of the license creation, including success status and any unobtainable reasons.
     */
    abstract suspend fun createLicense(identityLicense: IdentityLicense): IdentityLicense

    /**
     * Confiscates a license from the given identity.
     *
     * @param identity the identity from which the license will be confiscated.
     * @param license the license to be confiscated.
     * @param confiscatedBy the player who is confiscating the license.
     * @param confiscatedReason the reason for confiscation.
     * @return a result indicating whether the confiscation was successful.
     */
    abstract suspend fun confiscateLicense(
        identity: RpIdentity,
        license: License,
        confiscatedBy: RpPlayer,
        confiscatedReason: String
    ): Boolean

    /**
     * Removes a license from the given identity.
     *
     * @param identity the identity from which the license will be removed.
     * @param license the license to be removed.
     * @return true if the removal was successful, false otherwise.
     */
    abstract suspend fun removeLicense(
        identity: RpIdentity,
        license: License
    ): Boolean

    /**
     * Retrieves all expired licenses grouped by their respective identities.
     *
     * This method scans through the available licenses and identifies those
     * that have expired. The expired licenses are returned as a mapping
     * of identities to their associated expired licenses.
     *
     * @return A map where each key is an [RpIdentity] and the corresponding value
     *         is a set of [IdentityLicense] objects representing the expired licenses
     *         for that identity.
     */
    abstract suspend fun getAllExpiredLicenses(): Object2ObjectMap<RpIdentity, ObjectSet<IdentityLicense>>

    companion object {
        val instance get() = InternalContextHolder.instance.context.getBean<LicenseService>()
    }
}

/**
 * Convenience function to get a license by its type, or throw an exception if not found.
 *
 * @param T the type of the license to retrieve.
 * @return the license instance if found.
 * @throws IllegalArgumentException if the license is not found.
 */
inline fun <reified T : License> LicenseService.getLicense() =
    getLicense(T::class.java)
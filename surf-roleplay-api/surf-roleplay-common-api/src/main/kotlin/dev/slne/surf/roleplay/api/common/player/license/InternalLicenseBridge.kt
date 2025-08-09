@file:OptIn(InternalRoleplayApi::class)

package dev.slne.surf.roleplay.api.common.player.license

import dev.slne.surf.roleplay.api.common.InternalContextHolder
import dev.slne.surf.roleplay.api.common.player.RpPlayer
import dev.slne.surf.roleplay.api.common.player.identity.RpIdentity
import dev.slne.surf.roleplay.api.common.util.InternalRoleplayApi
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.key.Key
import org.jetbrains.annotations.Unmodifiable
import org.springframework.beans.factory.getBean

interface InternalLicenseBridge {

    /**
     * All licenses available in the game.
     */
    val licenses: @Unmodifiable ObjectSet<License>

    /**
     * Returns the license by its class.
     *
     * @param license the class of the license to retrieve.
     * @return the license instance if found, or null if not found.
     */
    fun getLicense(license: Class<out License>): License?

    /**
     * Returns the license by its class.
     *
     * @param license the class of the license to retrieve.
     * @return the license instance if found, or null if not found.
     * @throws IllegalArgumentException if the license is not found.
     */
    fun getLicenseOrThrow(license: Class<out License>): License

    /**
     * Returns the license by its key.
     *
     * @param key the key of the license to retrieve.
     * @return the license instance if found, or null if not found.
     */
    fun getLicenseByKey(key: Key): License?

    /**
     * Returns the license by its key.
     *
     * @param key the key of the license to retrieve.
     * @return the license instance if found.
     * @throws IllegalArgumentException if the license is not found.
     */
    fun getLicenseByKeyOrThrow(key: Key): License

    /**
     * Creates a new license for the given identity.
     *
     * @param identityLicense the license to create.
     * @return the result of the license creation, including success status and any unobtainable reasons.
     */
    suspend fun createLicense(identityLicense: IdentityLicense): IdentityLicense

    /**
     * Confiscates a license from the given identity.
     *
     * @param identity the identity from which the license will be confiscated.
     * @param license the license to be confiscated.
     * @param confiscatedBy the player who is confiscating the license.
     * @param confiscatedReason the reason for confiscation.
     * @return a result indicating whether the confiscation was successful.
     */
    suspend fun confiscateLicense(
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
    suspend fun removeLicense(
        identity: RpIdentity,
        license: License
    ): Boolean

    companion object {
        val instance get() = InternalContextHolder.instance.context.getBean<InternalLicenseBridge>()
    }
}

/**
 * Convenience function to get a license by its type.
 *
 * @param T the type of the license to retrieve.
 * @return the license instance if found, or null if not found.
 */
inline fun <reified T : License> InternalLicenseBridge.getLicense() = getLicense(T::class.java)

/**
 * Convenience function to get a license by its type, or throw an exception if not found.
 *
 * @param T the type of the license to retrieve.
 * @return the license instance if found.
 * @throws IllegalArgumentException if the license is not found.
 */
inline fun <reified T : License> InternalLicenseBridge.getLicenseOrThrow() =
    getLicenseOrThrow(T::class.java)
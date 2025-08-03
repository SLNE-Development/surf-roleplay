package dev.slne.surf.roleplay.api.mechanic.license

import dev.slne.surf.roleplay.api.mechanic.Mechanic
import dev.slne.surf.roleplay.api.mechanic.license.player.LicensePlayer
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.key.Key

interface LicenseMechanic : Mechanic {
    /**
     * A set of all registered [License] instances.
     */
    val licenses: ObjectSet<License>

    /**
     * Gets the [License] by its class type.
     *
     * @param license The class type of the license.
     * @return The [License] instance.
     */
    fun getLicense(license: Class<License>): License

    /**
     * Gets the [License] by its name.
     *
     * @param name The name of the license.
     * @return The [License] instance or throws an exception if not found.
     */
    fun getLicenseByKey(name: Key): License

    /**
     * Confiscates the specified [license] from the [player].
     * This method also removes all children licenses of the specified license.
     *
     * @param player The player from whom the license will be confiscated.
     * @param license The license to be confiscated.
     * @return `true` if the license was successfully confiscated, `false` otherwise.
     */
    suspend fun confiscateLicense(player: LicensePlayer, license: License): Boolean
}

/**
 * Gets the [License] by its class type.
 *
 * @param T The type of the license.
 * @return The [License] instance of type [T].
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T : License> LicenseMechanic.getLicense(): T =
    getLicense(T::class.java as Class<License>) as T
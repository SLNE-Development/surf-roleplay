package dev.slne.surf.roleplay.api.mechanic.license

import dev.slne.surf.roleplay.api.mechanic.Mechanic
import dev.slne.surf.surfapi.core.api.util.requiredService
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.key.Key

private val licenseMechanic = requiredService<LicenseMechanic>()

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
    fun getLicense(license: Class<out License>): License

    /**
     * Gets the [License] by its name.
     *
     * @param name The name of the license.
     * @return The [License] instance or throws an exception if not found.
     */
    fun getLicenseByKey(name: Key): License

    companion object : LicenseMechanic by licenseMechanic {
        val INSTANCE get() = licenseMechanic
    }
}

/**
 * Gets the [License] by its class type.
 *
 * @param T The type of the license.
 * @return The [License] instance of type [T].
 */
inline fun <reified T : License> LicenseMechanic.getLicense(): T =
    getLicense(T::class.java) as T
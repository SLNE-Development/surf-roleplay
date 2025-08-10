package dev.slne.surf.roleplay.api.common.mechanic

import dev.slne.surf.surfapi.core.api.util.requiredService

private val mechanicRegistry = requiredService<MechanicRegistry>()

interface MechanicRegistry {

    /**
     * Returns the mechanic instance of the specified class.
     *
     * @param clazz The class of the mechanic to retrieve.
     * @return The mechanic instance of the specified class.
     * @throws IllegalArgumentException if no mechanic of the specified class is registered.
     */
    fun <T : Mechanic> getMechanic(clazz: Class<T>): T

    companion object : MechanicRegistry by mechanicRegistry {
        val INSTANCE get() = mechanicRegistry
    }
}

/**
 * Returns the mechanic instance of the specified type.
 * This is a convenience function that uses reified type parameters
 *
 * @return The mechanic instance of the specified type
 */
inline fun <reified T : Mechanic> MechanicRegistry.getMechanic(): T = getMechanic(T::class.java)
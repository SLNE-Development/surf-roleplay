package dev.slne.surf.roleplay.core.common.mechanics

import dev.slne.surf.cloud.api.common.util.forEachAnnotationOrdered
import org.springframework.beans.factory.ObjectProvider
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class MechanicRegistry(
    private val context: ApplicationContext,
    private val mechanics: ObjectProvider<Mechanic>,
) {

    /**
     * Returns the mechanic instance of the specified class.
     *
     * @param clazz The class of the mechanic to retrieve.
     * @return The mechanic instance of the specified class.
     * @throws IllegalArgumentException if no mechanic of the specified class is registered.
     */
    fun <T : Mechanic> getMechanic(clazz: Class<T>) = context.getBean(clazz)

    suspend fun loadMechanics() {
        mechanics.forEachAnnotationOrdered { it.onLoad() }
    }

    suspend fun enableMechanics() {
        mechanics.forEachAnnotationOrdered { it.onEnable() }
    }

    suspend fun disableMechanics() {
        mechanics.forEachAnnotationOrdered { it.onDisable() } // // FIXME: 11.08.2025 20:27 turn around stream
    }
}

inline fun <reified T : Mechanic> MechanicRegistry.getMechanic(): T = getMechanic(T::class.java)
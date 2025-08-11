package dev.slne.surf.roleplay.core.common.mechanics

import dev.slne.surf.cloud.api.common.util.forEachAnnotationOrdered
import dev.slne.surf.roleplay.api.common.mechanic.Mechanic
import dev.slne.surf.roleplay.api.common.mechanic.MechanicRegistry
import org.springframework.beans.factory.ObjectProvider
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class CommonMechanicRegistry(
    private val context: ApplicationContext,
    private val mechanics: ObjectProvider<Mechanic>,
) : MechanicRegistry {

    override fun <T : Mechanic> getMechanic(clazz: Class<T>) = context.getBean(clazz)

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
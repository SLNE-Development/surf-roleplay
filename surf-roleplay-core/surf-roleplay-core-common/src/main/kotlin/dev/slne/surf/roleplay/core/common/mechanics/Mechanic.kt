@file:OptIn(InternalRoleplayApi::class)

package dev.slne.surf.roleplay.core.common.mechanics

import dev.slne.surf.roleplay.core.common.util.InternalContextHolder
import dev.slne.surf.roleplay.core.common.util.InternalRoleplayApi
import org.springframework.beans.factory.getBean

abstract class Mechanic(
    val name: String,
) {

    /**
     * Called when the mechanic is loaded.
     */
    open suspend fun onLoad() {

    }

    /**
     * Called when the mechanic is enabled.
     */
    open suspend fun onEnable() {

    }

    /**
     * Called when the mechanic is disabled.
     */
    open suspend fun onDisable() {

    }

    companion object {
        /**
         * Gets the mechanic by its class type.
         *
         * @param T The type of the mechanic.
         * @param mechanic The class type of the mechanic.
         * @return The [Mechanic] instance of type [T].
         */
        fun <T : Mechanic> getMechanic(mechanic: Class<out T>): T =
            InternalContextHolder.instance.context.getBean<MechanicRegistry>().getMechanic(mechanic)

        /**
         * Gets the mechanic by its class type.
         *
         * @param T The type of the mechanic.
         * @return The [Mechanic] instance of type [T].
         */
        inline fun <reified T : Mechanic> getMechanic(): T =
            getMechanic(T::class.java)
    }
}
package dev.slne.surf.roleplay.api.mechanic

import it.unimi.dsi.fastutil.objects.ObjectSet
import org.bukkit.event.Listener

interface Mechanic {

    /**
     * Returns the name of the mechanic.
     * This name should be unique across all mechanics.
     */
    val name: String

    /**
     * Returns an object set of handlers for this mechanic.
     */
    val handlers: ObjectSet<Listener>

    /**
     * Called when the mechanic is loaded.
     */
    suspend fun onLoad()

    /**
     * Called when the mechanic is enabled.
     */
    suspend fun onEnable()

    /**
     * Called when the mechanic is disabled.
     */
    suspend fun onDisable()

    companion object {
        /**
         * Gets the mechanic by its class type.
         *
         * @param T The type of the mechanic.
         * @param mechanic The class type of the mechanic.
         * @return The [Mechanic] instance of type [T].
         */
        fun <T : Mechanic> getMechanic(mechanic: Class<out T>): T =
            MechanicRegistry.getMechanic(mechanic)

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
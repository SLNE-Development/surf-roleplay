package dev.slne.surf.roleplay.api.mechanic.map

import dev.slne.surf.roleplay.api.mechanic.Mechanic
import dev.slne.surf.roleplay.api.mechanic.rentable.Rentable
import dev.slne.surf.surfapi.bukkit.api.builder.LoreBuilder
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.ComponentLike

interface Map : ComponentLike {

    /**
     * The key that uniquely identifies this map.
     */
    val key: Key

    /**
     * The name of the map.
     */
    val name: String

    /**
     * The lore of the map, which is a description that can be displayed in the UI.
     * This is built using a [LoreBuilder].
     */
    val description: LoreBuilder.() -> Unit

    /**
     * The display name of the map, which is used in the UI.
     */
    val displayName: SurfComponentBuilder.() -> Unit

    /**
     * A set of rentable properties associated with this map.
     */
    val rentables: ObjectSet<Rentable>

    companion object {
        /**
         * Retrieves a map of the specified type [T] from the [MapMechanic].
         *
         * @param clazz The class of the map type to retrieve.
         * @return An instance of the specified map type [T].
         */
        @Suppress("UNCHECKED_CAST")
        fun <T : Map> getMap(clazz: Class<out T>): T =
            Mechanic.getMechanic<MapMechanic>().getMap(clazz) as T
    }
}

/**
 * Convenience function to get a map of a specific type.
 *
 * @param T The type of the map to retrieve. It must be a subclass of [Map].
 * @return An instance of the specified map type [T].
 */
inline fun <reified T : Map> Map.Companion.getMap(): T = getMap(T::class.java)
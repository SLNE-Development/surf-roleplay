package dev.slne.surf.roleplay.api.mechanic.map

import dev.slne.surf.roleplay.api.mechanic.Mechanic
import dev.slne.surf.roleplay.api.mechanic.map.vote.MapVote
import it.unimi.dsi.fastutil.objects.ObjectSet

/**
 * Represents a mechanic that is related to the map in the game.
 */
interface MapMechanic : Mechanic {

    /**
     * The current map being played in the game.
     */
    val currentMap: Map

    /**
     * The current map vote in progress, if any.
     */
    val currentMapVote: MapVote?

    /**
     * A set of all maps available in the game.
     */
    val maps: ObjectSet<Map>

    /**
     * Retrieves a map of the specified class type.
     *
     * @param T The type of the map to retrieve.
     * @param clazz The class of the map to retrieve.
     * @return The map of the specified type.
     */
    fun <T : Map> getMap(clazz: Class<out T>): Map
}

/**
 * Retrieves a map of the specified class type using reified type parameters.
 *
 * @param T The type of the map to retrieve.
 * @return The map of the specified type.
 */
inline fun <reified T : Map> MapMechanic.getMap() = getMap(T::class.java)
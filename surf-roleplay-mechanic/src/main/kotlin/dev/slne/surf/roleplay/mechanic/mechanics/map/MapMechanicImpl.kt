package dev.slne.surf.roleplay.mechanic.mechanics.map

import dev.slne.surf.roleplay.api.mechanic.map.Map
import dev.slne.surf.roleplay.api.mechanic.map.MapMechanic
import dev.slne.surf.roleplay.api.mechanic.map.getMap
import dev.slne.surf.roleplay.api.mechanic.map.maps.RockfordMap
import dev.slne.surf.roleplay.api.mechanic.map.vote.MapVote
import dev.slne.surf.roleplay.mechanic.MechanicImpl
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import kotlin.time.Duration

class MapMechanicImpl(
) : MechanicImpl(
    name = "MapMechanic",
    handlers = objectSetOf(),
    rpJobs = objectSetOf()
), MapMechanic {

    override var currentMap: Map = Map.getMap<RockfordMap>()
        private set

    private val _maps = objectSetOf<Map>()
    override val maps get() = _maps.freeze()

    override var currentMapVote: MapVote? = null

    override fun <T : Map> getMap(clazz: Class<out T>) =
        _maps.firstOrNull { clazz.isAssignableFrom(it.javaClass) }
            ?: error("No map found for $clazz")

    override suspend fun changeMap(map: Map, restartAfter: Duration): Boolean {
        currentMap = map

        TODO("Implement this method and start a cancellable task to restart the server after the specified duration.")
    }
}
package dev.slne.surf.roleplay.mechanic.mechanics.map

import dev.slne.surf.roleplay.api.mechanic.map.Map
import dev.slne.surf.roleplay.api.mechanic.map.MapMechanic
import dev.slne.surf.roleplay.api.mechanic.map.vote.MapVote
import dev.slne.surf.roleplay.mechanic.MechanicImpl
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object MapMechanicImpl : MechanicImpl(
    name = "MapMechanic",
    handlers = objectSetOf(),
    rpJobs = objectSetOf()
), MapMechanic {
    
    private val _maps = objectSetOf<Map>()
    override val maps get() = _maps.freeze()

    override var currentMapVote: MapVote? = null

    override fun <T : Map> getMap(clazz: Class<out T>) =
        _maps.firstOrNull { clazz.isAssignableFrom(it.javaClass) }
            ?: error("No map found for $clazz")
}
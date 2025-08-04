package dev.slne.surf.roleplay.api.mechanic.map.event

import dev.slne.surf.roleplay.api.events.CancellableRpEvent
import dev.slne.surf.roleplay.api.mechanic.map.Map
import dev.slne.surf.roleplay.api.player.RpPlayer

/**
 * Event that is called when a player casts a vote for a map.
 *
 * @property map The map that the player is voting for.
 * @property player The player who is casting the vote.
 * @property amount The amount of votes cast by the player.
 */
class MapVoteCastEvent(
    val map: Map,
    val player: RpPlayer,
    var amount: Int
) : CancellableRpEvent()
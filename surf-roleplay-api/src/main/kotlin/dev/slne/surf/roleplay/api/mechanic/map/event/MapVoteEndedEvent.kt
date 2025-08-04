package dev.slne.surf.roleplay.api.mechanic.map.event

import dev.slne.surf.roleplay.api.events.RpEvent
import dev.slne.surf.roleplay.api.mechanic.map.Map
import dev.slne.surf.roleplay.api.mechanic.map.vote.MapVote

/**
 * Event that is called when a map vote ends.
 */
class MapVoteEndedEvent(
    var winningMap: Map?,
    var totalVotes: Int,
    val endedReason: MapVote.MapVoteEndedReason
) : RpEvent()
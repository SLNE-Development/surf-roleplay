package dev.slne.surf.roleplay.api.paper.player.events

import dev.slne.surf.roleplay.api.common.player.RpPlayer
import dev.slne.surf.roleplay.api.paper.events.RpEvent

class RpPlayerQuitEvent(
    val player: RpPlayer,
) : RpEvent()
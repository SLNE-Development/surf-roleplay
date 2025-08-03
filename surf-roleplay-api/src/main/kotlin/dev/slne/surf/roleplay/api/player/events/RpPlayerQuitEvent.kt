package dev.slne.surf.roleplay.api.player.events

import dev.slne.surf.roleplay.api.events.RpEvent
import dev.slne.surf.roleplay.api.player.RpPlayer

class RpPlayerQuitEvent(
    val rpPlayer: RpPlayer,
) : RpEvent()
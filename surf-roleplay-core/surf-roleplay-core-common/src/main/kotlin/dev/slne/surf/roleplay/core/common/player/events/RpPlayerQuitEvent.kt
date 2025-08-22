package dev.slne.surf.roleplay.core.common.player.events

import dev.slne.surf.cloud.api.common.event.CloudEvent
import dev.slne.surf.roleplay.core.common.player.RpPlayer

class RpPlayerQuitEvent(
    source: Any,
    val player: RpPlayer,
) : CloudEvent(source)
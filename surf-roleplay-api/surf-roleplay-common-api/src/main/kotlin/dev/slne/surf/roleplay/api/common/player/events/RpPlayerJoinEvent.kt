package dev.slne.surf.roleplay.api.common.player.events

import dev.slne.surf.cloud.api.common.event.CloudEvent
import dev.slne.surf.roleplay.api.common.player.RpPlayer

class RpPlayerJoinEvent(
    source: Any,
    val player: RpPlayer,
) : CloudEvent(source)
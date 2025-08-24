package dev.slne.surf.roleplay.paper.player.events

import dev.slne.surf.cloud.api.common.event.CloudEvent
import dev.slne.surf.roleplay.paper.player.PaperRpPlayer

class RpPlayerJoinEvent(
    source: Any,
    val player: PaperRpPlayer,
) : CloudEvent(source)
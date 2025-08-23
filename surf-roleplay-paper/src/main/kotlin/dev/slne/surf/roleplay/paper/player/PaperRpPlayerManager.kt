package dev.slne.surf.roleplay.paper.player

import dev.slne.surf.roleplay.core.common.player.RpPlayerManager
import dev.slne.surf.roleplay.paper.player.identity.RpIdentity
import dev.slne.surf.surfapi.bukkit.api.extensions.server
import org.springframework.stereotype.Component
import java.util.*

@Component
class PaperRpPlayerManager : RpPlayerManager() {
    override fun createPlayer(uuid: UUID) = PaperRpPlayer(uuid)
    override fun onlineUuids() = server.onlinePlayers.map { it.uniqueId }


}
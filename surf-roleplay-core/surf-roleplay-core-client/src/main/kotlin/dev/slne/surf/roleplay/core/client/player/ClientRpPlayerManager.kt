package dev.slne.surf.roleplay.core.client.player

import dev.slne.surf.roleplay.core.common.player.CommonRpPlayerManager
import org.springframework.stereotype.Component
import java.util.*

@Component
class ClientRpPlayerManager : CommonRpPlayerManager() {
    override fun createPlayer(uuid: UUID) = ClientRpPlayer(uuid)
}
package dev.slne.surf.roleplay.server.player

import dev.slne.surf.cloud.api.common.player.CloudPlayerManager
import dev.slne.surf.roleplay.core.common.player.RpPlayerManager
import org.springframework.stereotype.Component
import java.util.*

@Component
class RpPlayerManagerImpl(private val rpPlayerService: RpPlayerService) : RpPlayerManager() {

    override fun createPlayer(uuid: UUID) = ServerRpPlayer(uuid)
    override fun onlineUuids() = CloudPlayerManager.getOnlinePlayers().uuidSnapshot()

//    suspend fun updateUsername(
//        rpPlayer: RpPlayerImpl,
//        username: String
//    ): Unit = newSuspendedTransaction(Dispatchers.IO) {
//        rpPlayer.username = username
//        rpPlayer.updatedAt = ZonedDateTime.now()
//
//        RpPlayerModel.findSingleByAndUpdate((RpPlayerTable.uuid eq rpPlayer.uuid)) {
//            it.username = username
//            it.updatedAt = rpPlayer.updatedAt
//        }
//    }
}
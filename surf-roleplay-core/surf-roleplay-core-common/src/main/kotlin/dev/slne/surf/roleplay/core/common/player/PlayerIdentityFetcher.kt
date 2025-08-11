package dev.slne.surf.roleplay.core.common.player

import dev.slne.surf.cloud.api.common.player.OfflineCloudPlayer
import dev.slne.surf.cloud.api.common.player.task.PrePlayerJoinTask
import dev.slne.surf.roleplay.api.common.player.rpPlayer
import dev.slne.surf.surfapi.core.api.util.logger
import org.springframework.stereotype.Component

@Component
class PlayerIdentityFetcher : PrePlayerJoinTask {
    private val log = logger()

    override suspend fun preJoin(player: OfflineCloudPlayer): PrePlayerJoinTask.Result {
        try {
            (player.rpPlayer() as CommonRpPlayer).fetchIdentities()
            return PrePlayerJoinTask.Result.ALLOWED
        } catch (e: Throwable) {
            log.atSevere()
                .withCause(e)
                .log("Failed to fetch player identity for ${player.uuid}")
            return PrePlayerJoinTask.Result.ERROR
        }
    }
}
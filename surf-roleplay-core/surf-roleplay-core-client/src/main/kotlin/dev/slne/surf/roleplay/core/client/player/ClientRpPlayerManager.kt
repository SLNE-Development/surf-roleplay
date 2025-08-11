package dev.slne.surf.roleplay.core.client.player

import dev.slne.surf.roleplay.api.common.player.identity.RpIdentity
import dev.slne.surf.roleplay.core.common.player.CommonRpPlayerManager
import org.springframework.stereotype.Component
import java.util.*

@Component
class ClientRpPlayerManager : CommonRpPlayerManager() {
    override fun createPlayer(uuid: UUID) = ClientRpPlayer(uuid)
    
    override suspend fun fetchIdentities(uuid: UUID): Set<RpIdentity> {
        TODO("Not yet implemented")
    }

    override suspend fun <T : RpIdentity> createIdentity(
        identity: T
    ): T {
        TODO("Not yet implemented")
    }

    override suspend fun <T : RpIdentity> updateIdentity(
        identity: T
    ): T? {
        TODO("Not yet implemented")
    }

    override suspend fun <T : RpIdentity> createOrUpdateIdentity(
        identity: T
    ): T {
        TODO("Not yet implemented")
    }
}
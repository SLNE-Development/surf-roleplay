package dev.slne.surf.roleplay.core.client.player

import dev.slne.surf.roleplay.core.common.player.RpPlayerManager
import dev.slne.surf.roleplay.core.common.player.identity.RpIdentity
import dev.slne.surf.roleplay.core.common.util.InternalRoleplayApi
import org.springframework.stereotype.Component
import java.util.*

@OptIn(InternalRoleplayApi::class)
@Component
class ClientRpPlayerManager : RpPlayerManager() {
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
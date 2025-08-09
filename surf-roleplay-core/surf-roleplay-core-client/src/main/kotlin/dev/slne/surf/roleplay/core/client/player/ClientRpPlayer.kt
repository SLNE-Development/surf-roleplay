package dev.slne.surf.roleplay.core.client.player

import dev.slne.surf.roleplay.api.common.player.identity.RpIdentity
import dev.slne.surf.roleplay.core.common.player.CommonRpPlayer
import java.util.*

class ClientRpPlayer(uuid: UUID) : CommonRpPlayer(uuid) {
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
package dev.slne.surf.roleplay.paper.player.identity

import dev.slne.surf.cloud.api.client.netty.packet.fireAndAwaitOrThrow
import dev.slne.surf.roleplay.core.common.network.packets.ServerboundCreateOrUpdateIdentityPacket
import dev.slne.surf.roleplay.core.common.network.packets.ServerboundFetchIdentitiesByUuidPacket
import org.springframework.stereotype.Service
import java.util.*

@Service
class PaperIdentityService {

    suspend fun fetchIdentities(uuid: UUID) = ServerboundFetchIdentitiesByUuidPacket(uuid)
        .fireAndAwaitOrThrow()
        .identies
        .map(RpIdentity::fromNetwork)
        .toSet()

    @Suppress("UNCHECKED_CAST")
    suspend fun <T : RpIdentity> createOrUpdateIdentity(
        identity: T
    ): T = ServerboundCreateOrUpdateIdentityPacket(identity.toNetwork())
        .fireAndAwaitOrThrow()
        .identity
        .let(RpIdentity::fromNetwork) as T
}
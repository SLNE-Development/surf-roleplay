package dev.slne.surf.roleplay.server.player.identity

import dev.slne.surf.cloud.api.common.meta.SurfNettyPacketHandler
import dev.slne.surf.roleplay.core.common.network.packets.FetchIdentitiesByUuidPacketResponse
import dev.slne.surf.roleplay.core.common.network.packets.IdentityResponsePacket
import dev.slne.surf.roleplay.core.common.network.packets.ServerboundCreateOrUpdateIdentityPacket
import dev.slne.surf.roleplay.core.common.network.packets.ServerboundFetchIdentitiesByUuidPacket
import org.springframework.stereotype.Component

@Component
class RpIdentityPacketListener(private val rpIdentityService: RpIdentityService) {

    @SurfNettyPacketHandler
    suspend fun handleFetchIdentitiesByUuid(packet: ServerboundFetchIdentitiesByUuidPacket) {
        val identities = rpIdentityService.fetchByUuid(packet.uuid)
        packet.respond(FetchIdentitiesByUuidPacketResponse(identities))
    }

    @SurfNettyPacketHandler
    suspend fun handleCreateOrUpdateIdentity(packet: ServerboundCreateOrUpdateIdentityPacket) {
        val created = rpIdentityService.createOrUpdateIdentity(packet.identity)
        packet.respond(IdentityResponsePacket(created))
    }
}
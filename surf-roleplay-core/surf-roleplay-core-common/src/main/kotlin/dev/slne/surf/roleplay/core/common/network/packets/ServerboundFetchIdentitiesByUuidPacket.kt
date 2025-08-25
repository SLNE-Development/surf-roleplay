package dev.slne.surf.roleplay.core.common.network.packets

import dev.slne.surf.cloud.api.common.meta.SurfNettyPacket
import dev.slne.surf.cloud.api.common.netty.network.protocol.PacketFlow
import dev.slne.surf.cloud.api.common.netty.packet.RespondingNettyPacket
import dev.slne.surf.cloud.api.common.netty.packet.ResponseNettyPacket
import dev.slne.surf.roleplay.core.common.player.identity.NetworkIdentity
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@SurfNettyPacket("roleplay:serverbound:fetch-identies-by-uuid", PacketFlow.SERVERBOUND)
@Serializable
class ServerboundFetchIdentitiesByUuidPacket(val uuid: @Contextual UUID) :
    RespondingNettyPacket<FetchIdentitiesByUuidPacketResponse>()

@SurfNettyPacket("roleplay:response:fetch-identies-by-uuid", PacketFlow.BIDIRECTIONAL)
@Serializable
class FetchIdentitiesByUuidPacketResponse(val identies: List<NetworkIdentity>) :
    ResponseNettyPacket()
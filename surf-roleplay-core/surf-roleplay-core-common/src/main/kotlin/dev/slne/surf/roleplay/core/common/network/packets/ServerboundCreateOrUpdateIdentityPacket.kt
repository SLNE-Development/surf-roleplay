package dev.slne.surf.roleplay.core.common.network.packets

import dev.slne.surf.cloud.api.common.meta.SurfNettyPacket
import dev.slne.surf.cloud.api.common.netty.network.protocol.PacketFlow
import dev.slne.surf.cloud.api.common.netty.packet.RespondingNettyPacket
import dev.slne.surf.roleplay.core.common.player.identity.NetworkIdentity
import kotlinx.serialization.Serializable

@SurfNettyPacket("roleplay:serverbound:create-or-update-identity", PacketFlow.SERVERBOUND)
@Serializable
class ServerboundCreateOrUpdateIdentityPacket(val identity: NetworkIdentity) :
    RespondingNettyPacket<IdentityResponsePacket>()
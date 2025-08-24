package dev.slne.surf.roleplay.core.common.network.packets

import dev.slne.surf.cloud.api.common.meta.SurfNettyPacket
import dev.slne.surf.cloud.api.common.netty.network.protocol.PacketFlow
import dev.slne.surf.cloud.api.common.netty.packet.ResponseNettyPacket
import dev.slne.surf.roleplay.core.common.player.identity.NetworkIdentity
import kotlinx.serialization.Serializable

@SurfNettyPacket("roleplay:response:identity", PacketFlow.BIDIRECTIONAL)
@Serializable
class IdentityResponsePacket(val identity: NetworkIdentity) : ResponseNettyPacket()
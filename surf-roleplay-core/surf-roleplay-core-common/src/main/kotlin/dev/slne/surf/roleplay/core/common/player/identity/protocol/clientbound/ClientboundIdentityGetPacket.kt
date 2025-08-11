package dev.slne.surf.roleplay.core.common.player.identity.protocol.clientbound

import dev.slne.surf.cloud.api.common.meta.SurfNettyPacket
import dev.slne.surf.cloud.api.common.netty.network.codec.StreamCodec
import dev.slne.surf.cloud.api.common.netty.network.protocol.PacketFlow
import dev.slne.surf.cloud.api.common.netty.packet.ResponseNettyPacket
import dev.slne.surf.cloud.api.common.netty.packet.packetCodec
import dev.slne.surf.cloud.api.common.netty.protocol.buffer.SurfByteBuf
import dev.slne.surf.cloud.api.common.netty.protocol.buffer.readEnum
import dev.slne.surf.roleplay.core.common.player.identity.CommonRpIdentity

@SurfNettyPacket("roleplay:identity_get", PacketFlow.CLIENTBOUND)
class ClientboundIdentityGetPacket(
    val identity: CommonRpIdentity
) : ResponseNettyPacket() {

    companion object {
        val STREAM_CODEC =
            packetCodec(ClientboundIdentityGetPacket::write, ::ClientboundIdentityGetPacket)
    }

    private constructor(buf: SurfByteBuf) : this(
        buf.readEnum<CommonRpIdentity.RpIdentityCodecType>().codec.decode(buf)
    )

    @Suppress("UNCHECKED_CAST")
    private fun write(buf: SurfByteBuf) {
        buf.writeEnum(identity.type)
        val codec = identity.codecType.codec as StreamCodec<SurfByteBuf, CommonRpIdentity>
        codec.encode(buf, identity)
    }
}
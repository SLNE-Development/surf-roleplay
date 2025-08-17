package dev.slne.roleplay.mod.common.network.packets

import dev.slne.roleplay.mod.common.network.utils.SurfByteBuf
import dev.slne.roleplay.mod.common.network.utils.codec.StreamCodec
import dev.slne.roleplay.mod.common.network.utils.codec.StreamDecoder
import dev.slne.roleplay.mod.common.network.utils.codec.StreamMemberEncoder
import dev.slne.roleplay.mod.common.network.utils.getPacketMeta
import io.netty.buffer.ByteBuf

abstract class RoleplayPacket() {

    @Transient
    private val meta = this::class.getPacketMeta()

    @Transient
    val id = meta.id

    abstract fun write(buffer: SurfByteBuf)

    abstract fun read(buffer: SurfByteBuf)

    companion object {
        @JvmStatic
        fun <B : ByteBuf, T : RoleplayPacket> codec(
            encoder: StreamMemberEncoder<B, T>,
            decoder: StreamDecoder<B, T>
        ): StreamCodec<B, T> = StreamCodec.ofMember(encoder, decoder)
    }

}
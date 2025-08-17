package dev.slne.roleplay.mod.common.network.utils.codec.kotlinx

import dev.slne.roleplay.mod.common.network.utils.SurfByteBuf
import dev.slne.surf.bytebufserializer.KBufSerializer

abstract class RoleplayBufSerializer<T> : KBufSerializer<T, SurfByteBuf> {
    override val bufClass = SurfByteBuf::class
}
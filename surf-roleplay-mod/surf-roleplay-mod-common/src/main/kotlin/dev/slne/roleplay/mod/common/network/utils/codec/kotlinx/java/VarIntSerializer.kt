package dev.slne.roleplay.mod.common.network.utils.codec.kotlinx.java

import dev.slne.roleplay.mod.common.network.utils.SurfByteBuf
import dev.slne.roleplay.mod.common.network.utils.codec.kotlinx.RoleplayBufSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor

typealias IntAsVarInt = @Serializable(with = VarIntSerializer::class) Int

object VarIntSerializer : RoleplayBufSerializer<Int>() {
    override val descriptor = PrimitiveSerialDescriptor("VarInt", PrimitiveKind.INT)

    override fun serialize0(buf: SurfByteBuf, value: Int) {
        buf.writeVarInt(value)
    }

    override fun deserialize0(buf: SurfByteBuf): Int {
        return buf.readVarInt()
    }
}
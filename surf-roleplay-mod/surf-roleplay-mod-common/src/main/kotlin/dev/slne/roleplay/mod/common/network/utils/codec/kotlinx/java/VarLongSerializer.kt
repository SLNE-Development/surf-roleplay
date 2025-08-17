package dev.slne.roleplay.mod.common.network.utils.codec.kotlinx.java

import dev.slne.roleplay.mod.common.network.utils.SurfByteBuf
import dev.slne.roleplay.mod.common.network.utils.codec.kotlinx.RoleplayBufSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor

typealias LongAsVarLong = @Serializable(with = VarLongSerializer::class) Long

object VarLongSerializer : RoleplayBufSerializer<Long>() {
    override val descriptor = PrimitiveSerialDescriptor("VarLong", PrimitiveKind.LONG)

    override fun serialize0(buf: SurfByteBuf, value: Long) {
        buf.writeVarLong(value)
    }

    override fun deserialize0(buf: SurfByteBuf): Long {
        return buf.readVarLong()
    }
}
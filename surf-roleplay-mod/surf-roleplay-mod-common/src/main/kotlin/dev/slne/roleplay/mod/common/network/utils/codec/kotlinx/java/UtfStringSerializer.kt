package dev.slne.roleplay.mod.common.network.utils.codec.kotlinx.java

import dev.slne.roleplay.mod.common.network.utils.SurfByteBuf
import dev.slne.roleplay.mod.common.network.utils.codec.kotlinx.RoleplayBufSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor

typealias SerializableUtfString = @Serializable(with = UtfStringSerializer::class) String

object UtfStringSerializer : RoleplayBufSerializer<String>() {
    override val descriptor = PrimitiveSerialDescriptor("Utf8String", PrimitiveKind.STRING)

    override fun serialize0(buf: SurfByteBuf, value: String) {
        buf.writeUtf(value)
    }

    override fun deserialize0(buf: SurfByteBuf): String {
        return buf.readUtf()
    }
}
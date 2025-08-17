package dev.slne.roleplay.mod.common.network.utils.codec.kotlinx.java

import dev.slne.roleplay.mod.common.network.utils.SurfByteBuf
import dev.slne.roleplay.mod.common.network.utils.codec.kotlinx.RoleplayBufSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import java.util.*

typealias SerializableUUID = @Serializable(with = UUIDSerializer::class) UUID

object UUIDSerializer : RoleplayBufSerializer<UUID>() {
    override val descriptor = buildClassSerialDescriptor("UUID") {
        element<Long>("mostSignificantBits")
        element<Long>("leastSignificantBits")
    }

    override fun serialize0(
        buf: SurfByteBuf,
        value: UUID
    ) {
        buf.writeUuid(value)
    }

    override fun deserialize0(buf: SurfByteBuf): UUID {
        return buf.readUuid()
    }
}
package dev.slne.roleplay.mod.common.network.utils.codec.kotlinx.java

import dev.slne.roleplay.mod.common.network.utils.SurfByteBuf
import dev.slne.roleplay.mod.common.network.utils.codec.kotlinx.RoleplayBufSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import java.time.ZonedDateTime

typealias SerializableZonedDateTime = @Serializable(with = ZonedDateTimeSerializer::class) ZonedDateTime

object ZonedDateTimeSerializer : RoleplayBufSerializer<ZonedDateTime>() {
    override val descriptor = buildClassSerialDescriptor("ZonedDateTime") {
        element<Long>("epochMillis")
        element<String>("zoneId")
    }

    override fun deserialize0(buf: SurfByteBuf): ZonedDateTime {
        return buf.readZonedDateTime()
    }

    override fun serialize0(
        buf: SurfByteBuf,
        value: ZonedDateTime
    ) {
        buf.writeZonedDateTime(value)
    }
}
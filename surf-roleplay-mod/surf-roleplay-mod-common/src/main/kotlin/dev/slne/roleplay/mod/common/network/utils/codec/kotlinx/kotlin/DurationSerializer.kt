package dev.slne.roleplay.mod.common.network.utils.codec.kotlinx.kotlin

import dev.slne.roleplay.mod.common.network.utils.SurfByteBuf
import dev.slne.roleplay.mod.common.network.utils.codec.kotlinx.RoleplayBufSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlin.time.Duration

typealias SerializableDuration = @Serializable(with = DurationSerializer::class) Duration

object DurationSerializer : RoleplayBufSerializer<Duration>() {
    override val descriptor = PrimitiveSerialDescriptor("Duration", PrimitiveKind.LONG)

    override fun serialize0(
        buf: SurfByteBuf,
        value: Duration
    ) {
        buf.writeDuration(value)
    }

    override fun deserialize0(buf: SurfByteBuf): Duration {
        return buf.readDuration()
    }
}
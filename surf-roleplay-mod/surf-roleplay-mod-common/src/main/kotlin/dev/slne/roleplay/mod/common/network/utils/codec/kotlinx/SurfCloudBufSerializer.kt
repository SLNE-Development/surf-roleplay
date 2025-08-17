package dev.slne.roleplay.mod.common.network.utils.codec.kotlinx

import dev.slne.roleplay.mod.common.network.utils.codec.kotlinx.adventure.AdventureComponentSerializer
import dev.slne.roleplay.mod.common.network.utils.codec.kotlinx.java.*
import dev.slne.roleplay.mod.common.network.utils.codec.kotlinx.kotlin.DurationSerializer
import dev.slne.surf.bytebufserializer.Buf
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

object SurfCloudBufSerializer {
    val serializerModule = SerializersModule {
        // Adventure
        contextual(AdventureComponentSerializer)

        // Java
        contextual(UUIDSerializer)
        contextual(BitSetSerializer)
        contextual(UtfStringSerializer)
        contextual(ZonedDateTimeSerializer)
        contextual(BigDecimalSerializer)
        contextual(BigIntegerSerializer)

        // Kotlin
        contextual(DurationSerializer)
    }

    val serializer = Buf(serializerModule)
}
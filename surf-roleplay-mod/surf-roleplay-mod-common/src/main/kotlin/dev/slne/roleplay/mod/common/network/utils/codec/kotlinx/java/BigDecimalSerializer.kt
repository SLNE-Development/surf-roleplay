package dev.slne.roleplay.mod.common.network.utils.codec.kotlinx.java

import dev.slne.roleplay.mod.common.network.utils.SurfByteBuf
import dev.slne.roleplay.mod.common.network.utils.codec.kotlinx.RoleplayBufSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import java.math.BigDecimal

typealias SerializableBigDecimal = @Serializable(with = BigDecimalSerializer::class) BigDecimal

object BigDecimalSerializer : RoleplayBufSerializer<BigDecimal>() {
    override val descriptor = buildClassSerialDescriptor("dev.slne.surf.cloud.BigDecimal") {
        element<ByteArray>("unscaledValue")
        element<Int>("scale")
        element<Int>("precision")
    }

    override fun serialize0(buf: SurfByteBuf, value: BigDecimal) {
        buf.writeBigDecimal(value)
    }

    override fun deserialize0(buf: SurfByteBuf): BigDecimal {
        return buf.readBigDecimal()
    }
}
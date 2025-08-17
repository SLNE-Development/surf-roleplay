package dev.slne.roleplay.mod.common.network.utils.codec.kotlinx.java

import dev.slne.roleplay.mod.common.network.utils.SurfByteBuf
import dev.slne.roleplay.mod.common.network.utils.codec.kotlinx.RoleplayBufSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import java.util.*

typealias SerializableBitSet = @Serializable(with = BitSetSerializer::class) BitSet

object BitSetSerializer : RoleplayBufSerializer<BitSet>() {
    override val descriptor = buildClassSerialDescriptor("BitSet") {
        element<LongArray>("longArray")
    }

    override fun serialize0(
        buf: SurfByteBuf,
        value: BitSet
    ) {
        buf.writeBitSet(value)
    }

    override fun deserialize0(buf: SurfByteBuf): BitSet {
        return buf.readBitSet()
    }
}
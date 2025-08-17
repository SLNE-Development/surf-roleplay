package dev.slne.roleplay.mod.common.network.utils.codec.kotlinx.adventure

import dev.slne.roleplay.mod.common.network.utils.SurfByteBuf
import dev.slne.roleplay.mod.common.network.utils.codec.kotlinx.RoleplayBufSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import net.kyori.adventure.text.Component

typealias SerializableComponent = @Serializable(with = AdventureComponentSerializer::class) Component

object AdventureComponentSerializer : RoleplayBufSerializer<Component>() {
    override val descriptor = PrimitiveSerialDescriptor("Component", PrimitiveKind.STRING)

    override fun serialize0(
        buf: SurfByteBuf,
        value: Component
    ) {
        buf.writeComponent(value)
    }

    override fun deserialize0(buf: SurfByteBuf): Component {
        return buf.readComponent()
    }
}
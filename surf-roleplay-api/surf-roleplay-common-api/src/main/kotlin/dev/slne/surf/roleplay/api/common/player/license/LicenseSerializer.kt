@file:OptIn(InternalRoleplayApi::class)

package dev.slne.surf.roleplay.api.common.player.license

import dev.slne.surf.roleplay.api.common.util.InternalRoleplayApi
import dev.slne.surf.surfapi.core.api.messages.adventure.key
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object LicenseSerializer : KSerializer<License> {

    override val descriptor = PrimitiveSerialDescriptor("License", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: License
    ) {
        encoder.encodeString(value.key.asString())
    }

    override fun deserialize(decoder: Decoder) =
        InternalLicenseBridge.instance.getLicenseByKeyOrThrow(key(decoder.decodeString()))
}
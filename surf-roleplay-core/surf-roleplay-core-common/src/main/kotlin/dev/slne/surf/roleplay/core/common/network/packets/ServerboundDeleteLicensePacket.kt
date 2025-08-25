package dev.slne.surf.roleplay.core.common.network.packets

import dev.slne.surf.cloud.api.common.meta.SurfNettyPacket
import dev.slne.surf.cloud.api.common.netty.network.protocol.PacketFlow
import dev.slne.surf.cloud.api.common.netty.network.protocol.boolean.BooleanResponsePacket
import dev.slne.surf.roleplay.core.common.player.identity.RpIdentityType
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import net.kyori.adventure.key.Key
import java.util.*

@SurfNettyPacket("roleplay:serverbound:delete-license", PacketFlow.BIDIRECTIONAL)
@Serializable
class ServerboundDeleteLicensePacket(
    val uuid: @Contextual UUID,
    val identityType: RpIdentityType,
    val licenseKey: @Contextual Key
) : BooleanResponsePacket()
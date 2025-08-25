package dev.slne.surf.roleplay.core.common.network.packets

import dev.slne.surf.cloud.api.common.meta.SurfNettyPacket
import dev.slne.surf.cloud.api.common.netty.network.protocol.PacketFlow
import dev.slne.surf.cloud.api.common.netty.network.protocol.boolean.BooleanResponsePacket
import dev.slne.surf.roleplay.core.common.player.license.NetworkIdentityLicense
import kotlinx.serialization.Serializable

@SurfNettyPacket("roleplay:serverbound:create-license", PacketFlow.SERVERBOUND)
@Serializable
class ServerboundCreateLicensePacket(val license: NetworkIdentityLicense) : BooleanResponsePacket()
package dev.slne.surf.roleplay.mod.common.network.protocol.clientbound

import dev.slne.surf.roleplay.mod.common.network.SurfRoleplayPacket
import dev.slne.surf.roleplay.mod.common.network.packet.login.ClientboundRoleplayLoginPacket
import dev.slne.surf.roleplay.mod.common.network.protocol.DefaultIds
import dev.slne.surf.roleplay.mod.common.network.protocol.listener.login.ClientLoginPacketListener
import java.util.*

@SurfRoleplayPacket(id = DefaultIds.CLIENTBOUND_HANDSHAKE)
class ClientboundHandshakePacket(
    val handshake: UUID
) : ClientboundRoleplayLoginPacket {
    override fun handle(listener: ClientLoginPacketListener) {
        listener.handleHandshake(this)
    }
}
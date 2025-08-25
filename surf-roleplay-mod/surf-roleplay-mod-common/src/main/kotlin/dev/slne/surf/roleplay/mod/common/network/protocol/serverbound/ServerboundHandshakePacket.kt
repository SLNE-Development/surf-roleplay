package dev.slne.surf.roleplay.mod.common.network.protocol.serverbound

import dev.slne.surf.roleplay.mod.common.network.SurfRoleplayPacket
import dev.slne.surf.roleplay.mod.common.network.packet.login.ServerboundRoleplayLoginPacket
import dev.slne.surf.roleplay.mod.common.network.protocol.DefaultIds
import dev.slne.surf.roleplay.mod.common.network.protocol.listener.login.ServerLoginPacketListener
import java.util.*

@SurfRoleplayPacket(id = DefaultIds.SERVERBOUND_HANDSHAKE)
class ServerboundHandshakePacket(
    val handshake: UUID,
    val uuid: UUID,
    val clientVersion: String,
) : ServerboundRoleplayLoginPacket {
    override fun handle(listener: ServerLoginPacketListener) {
        listener.handleHandshake(this)
    }
}
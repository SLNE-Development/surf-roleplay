package dev.slne.surf.roleplay.mod.server.network

import dev.slne.surf.roleplay.mod.common.network.packet.login.ServerboundRoleplayLoginPacket
import dev.slne.surf.roleplay.mod.common.network.protocol.listener.packet.BasePacketHandler
import dev.slne.surf.roleplay.mod.server.network.listener.ServerLoginPacketListenerImpl
import me.mrnavastar.protoweaver.api.netty.ProtoConnection

class ServerLoginPacketHandler : BasePacketHandler<ServerLoginPacketListenerImpl>(
    { ServerLoginPacketListenerImpl(it) }
) {

    override fun onReady(connection: ProtoConnection) {
        super.onReady(connection)
        getHandlerOrThrow(connection).startVerifying()
    }

    override fun handlePacket(connection: ProtoConnection, packet: Any) {
        val handler = getHandlerOrThrow(connection)

        if (packet is ServerboundRoleplayLoginPacket) {
            packet.handle(handler)
        }
    }
}
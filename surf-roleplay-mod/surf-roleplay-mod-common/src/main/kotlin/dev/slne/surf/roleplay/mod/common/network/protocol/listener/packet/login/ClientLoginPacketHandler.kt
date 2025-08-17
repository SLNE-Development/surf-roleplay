package dev.slne.surf.roleplay.mod.common.network.protocol.listener.packet.login

import dev.slne.surf.roleplay.mod.common.network.NetworkBridge
import dev.slne.surf.roleplay.mod.common.network.packet.login.ClientboundRoleplayLoginPacket
import dev.slne.surf.roleplay.mod.common.network.protocol.listener.login.ClientLoginPacketListener
import dev.slne.surf.roleplay.mod.common.network.protocol.listener.packet.BasePacketHandler
import me.mrnavastar.protoweaver.api.netty.ProtoConnection

class ClientLoginPacketHandler : BasePacketHandler<ClientLoginPacketListener>(
    { NetworkBridge.createClientLoginPacketHandler(it) }
) {
    override fun handlePacket(connection: ProtoConnection, packet: Any) {
        val handler = getHandlerOrThrow(connection)

        if (packet is ClientboundRoleplayLoginPacket) {
            packet.handle(handler)
        }
    }
}
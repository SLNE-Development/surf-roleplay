package dev.slne.surf.roleplay.mod.network

import dev.slne.surf.roleplay.mod.RoleplayMod
import dev.slne.surf.roleplay.mod.common.network.protocol.clientbound.ClientboundHandshakePacket
import dev.slne.surf.roleplay.mod.common.network.protocol.listener.login.ClientLoginPacketListener
import dev.slne.surf.roleplay.mod.common.network.protocol.serverbound.ServerboundHandshakePacket

object ClientLoginPacketListenerImpl : ClientLoginPacketListener {
    private val connection
        get() = RoleplayMod.client?.connection ?: error("Not connected to a roleplay server")

    override fun handleHandshake(packet: ClientboundHandshakePacket) {
        connection.send(
            ServerboundHandshakePacket(
                handshake = packet.handshake,
                uuid = RoleplayMod.uuid,
                clientVersion = "1"
            )
        )
    }
}
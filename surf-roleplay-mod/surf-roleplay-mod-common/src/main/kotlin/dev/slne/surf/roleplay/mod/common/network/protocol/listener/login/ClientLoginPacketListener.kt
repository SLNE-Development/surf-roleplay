package dev.slne.surf.roleplay.mod.common.network.protocol.listener.login

import dev.slne.surf.roleplay.mod.common.network.protocol.clientbound.ClientboundHandshakePacket
import dev.slne.surf.roleplay.mod.common.network.protocol.listener.PacketListener

interface ClientLoginPacketListener : PacketListener.LoginPacketListener {
    fun handleHandshake(packet: ClientboundHandshakePacket)
}
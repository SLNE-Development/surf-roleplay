package dev.slne.surf.roleplay.mod.common.network.protocol.listener.login

import dev.slne.surf.roleplay.mod.common.network.protocol.listener.PacketListener
import dev.slne.surf.roleplay.mod.common.network.protocol.serverbound.ServerboundHandshakePacket

interface ServerLoginPacketListener : PacketListener.LoginPacketListener {
    fun handleHandshake(packet: ServerboundHandshakePacket)
}
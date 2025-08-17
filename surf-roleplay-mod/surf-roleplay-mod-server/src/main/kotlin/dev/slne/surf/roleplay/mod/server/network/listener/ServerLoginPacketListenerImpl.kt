package dev.slne.surf.roleplay.mod.server.network.listener

import dev.slne.surf.roleplay.mod.common.network.protocol.clientbound.ClientboundHandshakePacket
import dev.slne.surf.roleplay.mod.common.network.protocol.listener.TickablePacketListener
import dev.slne.surf.roleplay.mod.common.network.protocol.listener.login.ServerLoginPacketListener
import dev.slne.surf.roleplay.mod.common.network.protocol.serverbound.ServerboundHandshakePacket
import dev.slne.surf.roleplay.mod.common.network.user.userManager
import me.mrnavastar.protoweaver.api.netty.ProtoConnection
import java.util.*

class ServerLoginPacketListenerImpl(val connection: ProtoConnection) : ServerLoginPacketListener,
    TickablePacketListener {

    private var loginTime: Int = 0
    private val handshake = UUID.randomUUID()

    fun startVerifying() {
        connection.send(ClientboundHandshakePacket(handshake))
    }

    override fun tick() {
        if (loginTime++ >= LOGIN_TIMEOUT) {
            disconnect("Login timeout")
        }
    }

    override fun handleHandshake(packet: ServerboundHandshakePacket) {
        if (packet.handshake != handshake) {
            disconnect("Handshake mismatch")
            return
        }


        userManager.init(packet.uuid, connection)
    }

    private fun disconnect(reason: String) {

    }

    companion object {
        const val LOGIN_TIMEOUT = 30 // seconds
    }
}
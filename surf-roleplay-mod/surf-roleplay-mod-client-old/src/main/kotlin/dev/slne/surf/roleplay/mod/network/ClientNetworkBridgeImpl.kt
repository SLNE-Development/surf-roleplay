package dev.slne.surf.roleplay.mod.network

import com.google.auto.service.AutoService
import dev.slne.surf.roleplay.mod.RoleplayMod
import dev.slne.surf.roleplay.mod.common.network.NetworkBridge
import dev.slne.surf.roleplay.mod.common.network.protocol.listener.login.ClientLoginPacketListener
import dev.slne.surf.roleplay.mod.common.network.protocol.listener.login.ServerLoginPacketListener
import dev.slne.surf.roleplay.mod.common.network.protocol.listener.packet.login.ClientLoginPacketHandler
import me.mrnavastar.protoweaver.api.netty.ProtoConnection

@AutoService(NetworkBridge::class)
class ClientNetworkBridgeImpl : NetworkBridge {
    override val serverLoginPacketHandlerClass = null
    override val clientLoginPacketHandlerClass = ClientLoginPacketHandler::class.java

    override fun createServerLoginPacketHandler(connection: ProtoConnection): ServerLoginPacketListener {
        error("Client login packet handler is not implemented on the client side")
    }

    override fun createClientLoginPacketHandler(connection: ProtoConnection): ClientLoginPacketListener {
        require(connection == RoleplayMod.client?.connection) { "Connection mismatch" }
        return ClientLoginPacketListenerImpl
    }
}
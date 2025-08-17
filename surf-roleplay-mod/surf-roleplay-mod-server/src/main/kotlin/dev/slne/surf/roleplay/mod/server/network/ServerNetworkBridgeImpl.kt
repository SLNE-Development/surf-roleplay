package dev.slne.surf.roleplay.mod.server.network

import com.google.auto.service.AutoService
import dev.slne.surf.roleplay.mod.common.network.NetworkBridge
import dev.slne.surf.roleplay.mod.common.network.protocol.listener.login.ClientLoginPacketListener
import dev.slne.surf.roleplay.mod.server.network.listener.ServerLoginPacketListenerImpl
import me.mrnavastar.protoweaver.api.netty.ProtoConnection

@AutoService(NetworkBridge::class)
class ServerNetworkBridgeImpl : NetworkBridge {

    override val serverLoginPacketHandlerClass = ServerLoginPacketHandler::class.java
    override val clientLoginPacketHandlerClass = null

    override fun createServerLoginPacketHandler(connection: ProtoConnection) =
        ServerLoginPacketListenerImpl(connection)

    override fun createClientLoginPacketHandler(connection: ProtoConnection): ClientLoginPacketListener {
        error("Client login packet handler is not implemented on the server side")
    }
}
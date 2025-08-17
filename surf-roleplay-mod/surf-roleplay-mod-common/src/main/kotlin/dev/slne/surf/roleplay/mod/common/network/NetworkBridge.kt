package dev.slne.surf.roleplay.mod.common.network

import dev.slne.surf.roleplay.mod.common.network.protocol.listener.login.ClientLoginPacketListener
import dev.slne.surf.roleplay.mod.common.network.protocol.listener.login.ServerLoginPacketListener
import dev.slne.surf.surfapi.core.api.util.requiredService
import me.mrnavastar.protoweaver.api.ProtoConnectionHandler
import me.mrnavastar.protoweaver.api.netty.ProtoConnection

private val networkBridge = requiredService<NetworkBridge>()

interface NetworkBridge {

    val serverLoginPacketHandlerClass: Class<out ProtoConnectionHandler>?
    val clientLoginPacketHandlerClass: Class<out ProtoConnectionHandler>?

    fun createServerLoginPacketHandler(connection: ProtoConnection): ServerLoginPacketListener
    fun createClientLoginPacketHandler(connection: ProtoConnection): ClientLoginPacketListener

    companion object : NetworkBridge by networkBridge {
        val INSTANCE: NetworkBridge get() = networkBridge
    }
}
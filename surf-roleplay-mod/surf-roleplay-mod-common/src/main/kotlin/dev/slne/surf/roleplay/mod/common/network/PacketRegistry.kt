package dev.slne.surf.roleplay.mod.common.network

import dev.slne.surf.roleplay.mod.common.network.protocol.clientbound.ClientboundHandshakePacket
import dev.slne.surf.roleplay.mod.common.network.protocol.serverbound.ServerboundHandshakePacket
import me.mrnavastar.protoweaver.api.ProtoWeaver
import me.mrnavastar.protoweaver.api.protocol.CompressionType
import me.mrnavastar.protoweaver.api.protocol.Protocol

@Suppress("UNCHECKED_CAST")
object PacketRegistry {

    val loginProtocol: Protocol = registerLoginProtocol()
    val playProtocol: Protocol = registerPlayProtocol()

    init {
        ProtoWeaver.load(loginProtocol)
//        ProtoWeaver.load(playProtocol)
    }

    private fun getBaseProtocol(protocolName: String) =
        Protocol.create("surf-roleplay", protocolName)
            .setCompression(CompressionType.GZIP)

    private fun registerLoginProtocol() = getBaseProtocol("login")
        .apply {
            NetworkBridge.clientLoginPacketHandlerClass?.let { setServerHandler(it) }
            NetworkBridge.serverLoginPacketHandlerClass?.let { setClientHandler(it) }
        }
        .addPacket(ClientboundHandshakePacket::class.java)
        .addPacket(ServerboundHandshakePacket::class.java)
        .build()

    private fun registerPlayProtocol() = getBaseProtocol("play")
//        .setServerHandler()
//        .setClientHandler()
        .build()
}
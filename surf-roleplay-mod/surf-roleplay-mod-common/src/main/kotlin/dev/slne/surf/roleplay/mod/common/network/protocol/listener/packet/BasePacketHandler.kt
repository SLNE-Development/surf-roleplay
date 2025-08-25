package dev.slne.surf.roleplay.mod.common.network.protocol.listener.packet

import dev.slne.surf.roleplay.mod.common.network.protocol.listener.PacketListener
import dev.slne.surf.surfapi.core.api.util.logger
import me.mrnavastar.protoweaver.api.ProtoConnectionHandler
import me.mrnavastar.protoweaver.api.netty.ProtoConnection
import java.util.concurrent.ConcurrentHashMap

abstract class BasePacketHandler<P : PacketListener>(
    private val packetListenerFactory: (ProtoConnection) -> P
) : ProtoConnectionHandler {

    private val log = logger()

    private val _handlers = ConcurrentHashMap<ProtoConnection, P>()
    protected val handlers get() = _handlers.toMap()

    fun getHandlerOrThrow(connection: ProtoConnection) =
        _handlers[connection] ?: error("Unknown connection type: ${connection.remoteAddress}")

    override fun onReady(connection: ProtoConnection) {
        log.atInfo().log("New connection from ${connection.remoteAddress}")
        _handlers[connection] = packetListenerFactory(connection)
    }

    override fun onDisconnect(connection: ProtoConnection) {
        log.atInfo().log("Connection from ${connection.remoteAddress} disconnected")

        _handlers.remove(connection)
    }
}
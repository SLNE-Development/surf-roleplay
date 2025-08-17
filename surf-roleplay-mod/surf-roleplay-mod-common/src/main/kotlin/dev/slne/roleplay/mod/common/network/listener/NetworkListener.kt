package dev.slne.roleplay.mod.common.network.listener

import com.github.retrooper.packetevents.event.PacketListener
import com.github.retrooper.packetevents.event.PacketReceiveEvent
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import dev.slne.roleplay.mod.common.network.PacketRegistry
import dev.slne.roleplay.mod.common.network.utils.SurfByteBuf
import dev.slne.surf.surfapi.core.api.util.logger
import io.netty.buffer.ByteBuf
import kotlinx.coroutines.*

object NetworkListener : PacketListener {

    private val log = logger()

    private val supervisor = SupervisorJob()
    private val exceptionHandler = CoroutineExceptionHandler { context, throwable ->
        log.atSevere().withCause(throwable)
            .log("Exception in network listener coroutine: ${context[CoroutineName]?.name ?: "unknown"}")
    }
    private val scope =
        CoroutineScope(supervisor + CoroutineName("NetworkListener") + exceptionHandler)

    override fun onPacketReceive(event: PacketReceiveEvent) {
        if (!(event.packetType == PacketType.Play.Server.PLUGIN_MESSAGE ||
                    event.packetType == PacketType.Play.Client.PLUGIN_MESSAGE)
        ) return

        val eventBuffer = event.byteBuf as ByteBuf
        val buffer = SurfByteBuf(eventBuffer)

        val packetId = buffer.readUtf()

        scope.launch {
            PacketRegistry.callListeners(packetId, buffer)
        }
    }
}
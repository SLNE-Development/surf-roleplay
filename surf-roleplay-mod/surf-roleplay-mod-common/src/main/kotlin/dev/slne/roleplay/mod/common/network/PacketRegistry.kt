package dev.slne.roleplay.mod.common.network

import dev.slne.roleplay.mod.common.network.packets.RoleplayPacket
import dev.slne.roleplay.mod.common.network.protocol.DefaultIds
import dev.slne.roleplay.mod.common.network.protocol.clientbound.ClientboundHandshakePacket
import dev.slne.roleplay.mod.common.network.protocol.serverbound.ServerboundHandshakePacket
import dev.slne.roleplay.mod.common.network.utils.RoleplayPacketHandler
import dev.slne.roleplay.mod.common.network.utils.RoleplayPacketListener
import dev.slne.roleplay.mod.common.network.utils.SurfByteBuf
import dev.slne.surf.surfapi.core.api.util.logger
import dev.slne.surf.surfapi.core.api.util.mutableObject2ObjectMapOf
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import it.unimi.dsi.fastutil.objects.ObjectSet
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.callSuspend
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberFunctions

@Suppress("UNCHECKED_CAST")
object PacketRegistry {

    private val log = logger()
    
    private val packets = mutableObject2ObjectMapOf<String, KClass<out RoleplayPacket>>()
    private val listeners =
        mutableObject2ObjectMapOf<RoleplayPacketListener, ObjectSet<Pair<KFunction<*>, KClass<out RoleplayPacket>>>>()

    init {
        registerPacket(DefaultIds.CLIENTBOUND_HANDSHAKE, ClientboundHandshakePacket::class)
        registerPacket(DefaultIds.SERVERBOUND_HANDSHAKE, ServerboundHandshakePacket::class)
    }

    fun registerPacket(packetId: String, packetClass: KClass<out RoleplayPacket>) {
        packets[packetId] = packetClass
    }

    fun registerListener(listener: RoleplayPacketListener) {
        val methods = listener::class.memberFunctions
            .filter { it.hasAnnotation<RoleplayPacketHandler>() }
            .toObjectSet()

        val methodPairs = methods.map { method ->
            method to method.parameters.firstOrNull()?.type?.classifier as? KClass<out RoleplayPacket>
        }.filter { it.second != null }.map { it.first to it.second!! }.toObjectSet()

        listeners[listener] = methodPairs
    }

    fun getPacket(packetId: String) = packets[packetId]

    suspend fun callListeners(packetId: String, buffer: SurfByteBuf) {
        val packetClass = packets[packetId] ?: error("Packet with ID '$packetId' is not registered")
        val packet = packetClass.constructors.firstOrNull()?.call()
            ?: error("Failed to create packet instance for ID '$packetId' (no default constructor?)")

        packet.read(buffer)

        listeners.forEach { (_, methods) ->
            methods.forEach { (method, param) ->
                if (param.isInstance(packetClass)) {
                    try {
                        if (method.isSuspend) {
                            method.callSuspend(packet)
                        } else {
                            method.call(packet)
                        }
                    } catch (exception: Exception) {
                        log.atSevere().withCause(exception)
                            .log("Error while calling packet listener method: ${method.name} for packet ID: $packetId")
                    }
                }
            }
        }
    }

}
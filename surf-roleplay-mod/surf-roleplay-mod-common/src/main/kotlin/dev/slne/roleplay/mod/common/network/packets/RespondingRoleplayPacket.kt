package dev.slne.roleplay.mod.common.network.packets

import dev.slne.roleplay.mod.common.network.connection.Connection
import dev.slne.roleplay.mod.common.network.utils.SurfByteBuf
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

val DEFAULT_TIMEOUT = 15.seconds
val DEFAULT_URGENT_TIMEOUT = 5.seconds

abstract class RespondingRoleplayPacket<R : ResponseRoleplayPacket> : RoleplayPacket() {

    private var uniqueSessionId: UUID? = null
    lateinit var responseConnection: Connection

    val response = CompletableDeferred<R>()

    suspend fun fireAndAwait(
        connection: Connection,
        timeout: Duration = DEFAULT_TIMEOUT
    ): R? = withTimeoutOrNull(timeout) {
        connection.send(this@RespondingRoleplayPacket)
        response.await()
    }

    suspend fun fireAndAwaitUrgent(connection: Connection): R? =
        fireAndAwait(connection, DEFAULT_URGENT_TIMEOUT)

    suspend fun fireAndAwaitOrThrow(
        connection: Connection,
        timeout: Duration = DEFAULT_TIMEOUT
    ): R = withTimeout(timeout) {
        connection.send(this@RespondingRoleplayPacket)
        response.await()
    }

    suspend fun fireAndAwaitOrThrowUrgent(connection: Connection): R =
        fireAndAwaitOrThrow(connection, DEFAULT_URGENT_TIMEOUT)

    fun respond(packet: R) {
        packet.responseTo = uniqueSessionId
            ?: error("Responding packet has no session id. Are you sure it was sent?")
        responseConnection.send(packet)
    }

    override fun write(buffer: SurfByteBuf) {
        buffer.writeUuid(getUniqueSessionIdOrCreate())
    }

    override fun read(buffer: SurfByteBuf) {
        uniqueSessionId = buffer.readUuid()
    }

    fun getUniqueSessionIdOrCreate(): UUID =
        uniqueSessionId ?: UUID.randomUUID().also { uniqueSessionId = it }
}
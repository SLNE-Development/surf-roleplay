package dev.slne.roleplay.mod.common.network.connection

import dev.slne.roleplay.mod.common.network.packets.RoleplayPacket
import kotlinx.coroutines.CompletableDeferred

interface Connection {
    val receivedPackets: Int
    val sentPackets: Int
    val averageReceivedPackets: Float
    val averageSentPackets: Float

    val latency: Int

    /**
     * Sends a packet to the connection.
     *
     * @param packet The [RoleplayPacket] to send.
     * @param flush Whether to flush the packet immediately. Defaults to `true`.
     */
    fun send(packet: RoleplayPacket, flush: Boolean = true)

    /**
     * Sends a packet with a sign of success, suspending until the operation completes.
     *
     * @param packet The [RoleplayPacket] to send.
     * @param flush Whether to flush the packet immediately. Defaults to `true`.
     * @param convertExceptions Whether to convert exceptions.
     * @return `true` if the packet was sent successfully; `false` otherwise.
     */
    suspend fun sendWithIndication(
        packet: RoleplayPacket,
        flush: Boolean = true,
        convertExceptions: Boolean = true
    ): Boolean

    /**
     * Sends a packet with a [CompletableDeferred] indication of success.
     *
     * @param packet The [RoleplayPacket] to send.
     * @param flush Whether to flush the packet immediately. Defaults to `true`.
     * @param deferred The [CompletableDeferred] to complete upon success or failure.
     */
    fun sendWithIndication(
        packet: RoleplayPacket,
        flush: Boolean = true,
        deferred: CompletableDeferred<Boolean>
    )
}
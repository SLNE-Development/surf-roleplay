package dev.slne.roleplay.mod.common.network.protocol.serverbound

import dev.slne.roleplay.mod.common.network.SurfRoleplayPacket
import dev.slne.roleplay.mod.common.network.packets.ResponseRoleplayPacket
import dev.slne.roleplay.mod.common.network.protocol.DefaultIds
import dev.slne.roleplay.mod.common.network.utils.PacketFlow
import dev.slne.roleplay.mod.common.network.utils.SurfByteBuf
import java.util.*

@SurfRoleplayPacket(id = DefaultIds.SERVERBOUND_HANDSHAKE, flow = PacketFlow.SERVERBOUND)
class ServerboundHandshakePacket() : ResponseRoleplayPacket() {

    lateinit var handshake: UUID
    lateinit var clientVersion: String

    constructor(handshake: UUID, clientVersion: String) : this() {
        this.handshake = handshake
        this.clientVersion = clientVersion
    }

    override fun write(buffer: SurfByteBuf) {
        super.write(buffer)

        buffer.writeUuid(handshake)
        buffer.writeUtf(clientVersion)
    }

    override fun read(buffer: SurfByteBuf) {
        super.read(buffer)

        handshake = buffer.readUuid()
        clientVersion = buffer.readUtf()
    }
}
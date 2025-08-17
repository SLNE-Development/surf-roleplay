package dev.slne.roleplay.mod.common.network.protocol.clientbound

import dev.slne.roleplay.mod.common.network.SurfRoleplayPacket
import dev.slne.roleplay.mod.common.network.packets.RespondingRoleplayPacket
import dev.slne.roleplay.mod.common.network.protocol.DefaultIds
import dev.slne.roleplay.mod.common.network.protocol.serverbound.ServerboundHandshakePacket
import dev.slne.roleplay.mod.common.network.utils.PacketFlow
import dev.slne.roleplay.mod.common.network.utils.SurfByteBuf
import java.util.*

@SurfRoleplayPacket(id = DefaultIds.CLIENTBOUND_HANDSHAKE, flow = PacketFlow.CLIENTBOUND)
class ClientboundHandshakePacket() : RespondingRoleplayPacket<ServerboundHandshakePacket>() {

    lateinit var handshake: UUID

    constructor(handshake: UUID) : this() {
        this.handshake = handshake
    }

    override fun write(buffer: SurfByteBuf) {
        super.write(buffer)

        buffer.writeUuid(handshake)
    }

    override fun read(buffer: SurfByteBuf) {
        super.read(buffer)
        
        handshake = buffer.readUuid()
    }
}
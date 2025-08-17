package dev.slne.roleplay.mod.common.network.packets

import dev.slne.roleplay.mod.common.network.utils.SurfByteBuf
import java.util.*

abstract class ResponseRoleplayPacket() : RoleplayPacket() {

    lateinit var responseTo: UUID

    override fun write(buffer: SurfByteBuf) {
        buffer.writeUuid(responseTo)
    }

    override fun read(buffer: SurfByteBuf) {
        responseTo = buffer.readUuid()
    }
}
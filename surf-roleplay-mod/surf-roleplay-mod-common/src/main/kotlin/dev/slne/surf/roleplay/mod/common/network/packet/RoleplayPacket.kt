package dev.slne.surf.roleplay.mod.common.network.packet

import dev.slne.surf.roleplay.mod.common.network.SurfRoleplayPacket
import dev.slne.surf.roleplay.mod.common.network.protocol.listener.PacketListener

interface RoleplayPacket<L : PacketListener> {

    val meta: SurfRoleplayPacket
        get() = javaClass.getDeclaredAnnotation(SurfRoleplayPacket::class.java)

    val id: String get() = meta.id

    fun handle(listener: L)

}
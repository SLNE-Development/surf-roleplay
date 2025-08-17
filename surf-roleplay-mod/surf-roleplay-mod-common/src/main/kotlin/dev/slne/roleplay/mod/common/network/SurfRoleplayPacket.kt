package dev.slne.roleplay.mod.common.network

import dev.slne.roleplay.mod.common.network.utils.PacketFlow

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class SurfRoleplayPacket(
    val id: String,
    val flow: PacketFlow,
)
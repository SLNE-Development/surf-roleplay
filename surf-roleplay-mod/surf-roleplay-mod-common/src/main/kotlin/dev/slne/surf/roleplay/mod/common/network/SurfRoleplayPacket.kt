package dev.slne.surf.roleplay.mod.common.network

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class SurfRoleplayPacket(
    val id: String,
)
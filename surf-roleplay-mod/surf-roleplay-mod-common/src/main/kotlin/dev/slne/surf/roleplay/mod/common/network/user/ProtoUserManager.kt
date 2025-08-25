package dev.slne.surf.roleplay.mod.common.network.user

import com.github.benmanes.caffeine.cache.Caffeine
import dev.slne.surf.surfapi.core.api.util.requiredService
import me.mrnavastar.protoweaver.api.netty.ProtoConnection
import java.util.*

val userManager = requiredService<ProtoUserManager>()

abstract class ProtoUserManager {
    private val users = Caffeine.newBuilder()
        .build<UUID, ProtoUser>()


    fun init(uuid: UUID, connection: ProtoConnection) {
        users.put(uuid, createUser(uuid, connection))
    }

    fun destroy(uuid: UUID) {
        users.invalidate(uuid)
    }

    protected abstract fun createUser(uuid: UUID, connection: ProtoConnection): ProtoUser

    operator fun get(uuid: UUID): ProtoUser =
        users.getIfPresent(uuid) ?: error("No user exists for $uuid")
}
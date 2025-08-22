package dev.slne.surf.roleplay.core.common.player

import com.github.benmanes.caffeine.cache.Caffeine
import dev.slne.surf.roleplay.core.common.player.identity.RpIdentity
import dev.slne.surf.roleplay.core.common.util.InternalContextHolder
import dev.slne.surf.roleplay.core.common.util.InternalRoleplayApi
import org.springframework.beans.factory.getBean
import java.util.*

@InternalRoleplayApi
abstract class RpPlayerManager {

    private val cache = Caffeine.newBuilder()
        .maximumSize(10_000)
        .build<UUID, RpPlayer> { createPlayer(it) }

    abstract fun createPlayer(uuid: UUID): RpPlayer

    fun getPlayerByUuid(uuid: UUID) = cache.get(uuid)

    abstract suspend fun fetchIdentities(uuid: UUID): Set<RpIdentity>

    abstract suspend fun <T : RpIdentity> createIdentity(identity: T): T
    abstract suspend fun <T : RpIdentity> updateIdentity(identity: T): T?
    abstract suspend fun <T : RpIdentity> createOrUpdateIdentity(identity: T): T

    companion object {
        val instance get() = InternalContextHolder.instance.context.getBean<RpPlayerManager>()
    }
}
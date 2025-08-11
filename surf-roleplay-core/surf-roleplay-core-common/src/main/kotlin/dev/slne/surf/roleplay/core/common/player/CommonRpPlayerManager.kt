package dev.slne.surf.roleplay.core.common.player

import com.github.benmanes.caffeine.cache.Caffeine
import dev.slne.surf.roleplay.api.common.player.RpPlayerManager
import dev.slne.surf.roleplay.api.common.player.identity.RpIdentity
import dev.slne.surf.roleplay.api.common.util.InternalRoleplayApi
import java.util.*

@OptIn(InternalRoleplayApi::class)
abstract class CommonRpPlayerManager : RpPlayerManager {

    private val cache = Caffeine.newBuilder()
        .maximumSize(10_000)
        .build<UUID, CommonRpPlayer> { createPlayer(it) }

    abstract fun createPlayer(uuid: UUID): CommonRpPlayer

    override fun getPlayerByUuid(uuid: UUID) = cache.get(uuid)

    abstract suspend fun fetchIdentities(uuid: UUID): Set<RpIdentity>

    abstract suspend fun <T : RpIdentity> createIdentity(identity: T): T
    abstract suspend fun <T : RpIdentity> updateIdentity(identity: T): T?
    abstract suspend fun <T : RpIdentity> createOrUpdateIdentity(identity: T): T
}
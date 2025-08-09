package dev.slne.surf.roleplay.core.common.player

import com.github.benmanes.caffeine.cache.Caffeine
import dev.slne.surf.roleplay.api.common.player.RpPlayerManager
import dev.slne.surf.roleplay.api.common.util.InternalRoleplayApi
import java.util.*
import kotlin.time.Duration.Companion.hours

@OptIn(InternalRoleplayApi::class)
abstract class CommonRpPlayerManager : RpPlayerManager {

    private val cache = Caffeine.newBuilder()
        .expireAfterAccess(3.hours)
        .maximumSize(10_000)
        .build<UUID, CommonRpPlayer> { createPlayer(it) }

    abstract fun createPlayer(uuid: UUID): CommonRpPlayer

    override fun getPlayerByUuid(uuid: UUID) = cache.get(uuid)
}
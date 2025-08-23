package dev.slne.surf.roleplay.core.common.player

import com.github.benmanes.caffeine.cache.Caffeine
import com.sksamuel.aedile.core.expireAfterAccess
import dev.slne.surf.roleplay.core.common.player.identity.RpIdentity
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.minutes

@Component
abstract class RpPlayerManager {

    private val cache = Caffeine.newBuilder()
        .maximumSize(10_000)
        .expireAfterAccess(30.minutes)
        .build<UUID, RpPlayer> { createPlayer(it) }

    abstract fun createPlayer(uuid: UUID): RpPlayer
    abstract fun onlineUuids(): Set<UUID>

    fun getPlayerByUuid(uuid: UUID) = cache.get(uuid)

    abstract suspend fun fetchIdentities(uuid: UUID): Set<RpIdentity>

    abstract suspend fun <T : RpIdentity> createIdentity(identity: T): T
    abstract suspend fun <T : RpIdentity> updateIdentity(identity: T): T?
    abstract suspend fun <T : RpIdentity> createOrUpdateIdentity(identity: T): T

    @Scheduled(fixedDelay = 15, timeUnit = TimeUnit.MINUTES)
    fun cacheRefresh() {
        onlineUuids().forEach { uuid ->
            cache.getIfPresent(uuid)
        }
    }
}
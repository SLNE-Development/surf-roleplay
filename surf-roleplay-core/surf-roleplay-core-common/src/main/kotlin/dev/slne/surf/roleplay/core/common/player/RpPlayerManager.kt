package dev.slne.surf.roleplay.core.common.player

import com.github.benmanes.caffeine.cache.Caffeine
import com.sksamuel.aedile.core.expireAfterAccess
import dev.slne.surf.surfapi.core.api.util.toObjectList
import it.unimi.dsi.fastutil.objects.ObjectList
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

    val players: ObjectList<out RpPlayer> get() = cache.asMap().values.toObjectList()

    abstract fun createPlayer(uuid: UUID): RpPlayer
    abstract fun onlineUuids(): List<UUID>

    fun getPlayerByUuid(uuid: UUID) = cache.get(uuid)

    @Scheduled(fixedDelay = 15, timeUnit = TimeUnit.MINUTES)
    fun cacheRefresh() {
        onlineUuids().forEach { uuid ->
            cache.getIfPresent(uuid)
        }
    }
}
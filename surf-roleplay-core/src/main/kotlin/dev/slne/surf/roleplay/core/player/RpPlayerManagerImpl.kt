package dev.slne.surf.roleplay.core.player

import com.github.benmanes.caffeine.cache.Caffeine
import com.google.auto.service.AutoService
import dev.hsbrysk.caffeine.buildCoroutine
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.RpPlayerManager
import dev.slne.surf.roleplay.core.player.db.RpPlayerModel
import dev.slne.surf.roleplay.core.player.db.RpPlayerTable
import kotlinx.coroutines.Dispatchers
import net.kyori.adventure.util.Services
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

@AutoService(RpPlayerManager::class)
class RpPlayerManagerImpl : RpPlayerManager, Services.Fallback {

    private val cache = Caffeine.newBuilder()
        .expireAfterAccess(30.minutes.toJavaDuration())
        .buildCoroutine<UUID, RpPlayer> { key ->
            findOrCreate(key).toApi() as RpPlayer
        }

    private suspend fun findOrCreate(uuid: UUID) = newSuspendedTransaction(Dispatchers.IO) {
        RpPlayerModel.find { RpPlayerTable.uuid eq uuid }.singleOrNull() ?: RpPlayerModel.new {
            this.uuid = uuid
        }
    }

    override suspend fun get(uuid: UUID) = cache.get(uuid)
}
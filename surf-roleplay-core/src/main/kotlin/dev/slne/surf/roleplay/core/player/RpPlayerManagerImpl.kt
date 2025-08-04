package dev.slne.surf.roleplay.core.player

import com.github.benmanes.caffeine.cache.Caffeine
import com.google.auto.service.AutoService
import dev.hsbrysk.caffeine.buildCoroutine
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.RpPlayerInformation
import dev.slne.surf.roleplay.api.player.RpPlayerManager
import dev.slne.surf.roleplay.core.player.db.RpPlayerModel
import dev.slne.surf.roleplay.core.player.db.RpPlayerTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.await
import net.kyori.adventure.util.Services
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.ZonedDateTime
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

    suspend fun findOrCreate(uuid: UUID) = newSuspendedTransaction(Dispatchers.IO) {
        RpPlayerModel.find { RpPlayerTable.uuid eq uuid }.singleOrNull() ?: RpPlayerModel.new {
            this.uuid = uuid
            this.createdAt = ZonedDateTime.now()
            this.updatedAt = ZonedDateTime.now()
        }
    }

    suspend fun updatePlayerInformation(
        rpPlayer: RpPlayerImpl,
        update: RpPlayerInformation.() -> Unit
    ): Unit = newSuspendedTransaction(Dispatchers.IO) {
        rpPlayer.information.update()
        rpPlayer.updatedAt = ZonedDateTime.now()

        RpPlayerModel.findSingleByAndUpdate((RpPlayerTable.uuid eq rpPlayer.uuid)) {
            it.username = rpPlayer.username
            it.firstName = rpPlayer.information.firstName
            it.lastName = rpPlayer.information.lastName
            it.birthDate = rpPlayer.information.birthDate
            it.updatedAt = rpPlayer.updatedAt
        }
    }

    override suspend fun get(uuid: UUID) = cache.get(uuid)

    override suspend fun getByName(username: String) = newSuspendedTransaction(Dispatchers.IO) {
        val cacheValue = cache.asynchronous().asMap().values.map { it.await() }.find { it.username == username }

        if (cacheValue != null) {
            return@newSuspendedTransaction cacheValue
        }

        RpPlayerModel.find { RpPlayerTable.username eq username }.singleOrNull()?.toApi()
    }
}

val rpPlayerManagerImpl get() = RpPlayerManager.INSTANCE as RpPlayerManagerImpl
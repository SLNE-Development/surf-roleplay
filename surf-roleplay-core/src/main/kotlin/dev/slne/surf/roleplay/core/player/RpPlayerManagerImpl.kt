package dev.slne.surf.roleplay.core.player

import com.google.auto.service.AutoService
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.RpPlayerInformation
import dev.slne.surf.roleplay.api.player.RpPlayerManager
import dev.slne.surf.roleplay.core.player.db.RpPlayerModel
import dev.slne.surf.roleplay.core.player.db.RpPlayerTable
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import kotlinx.coroutines.Dispatchers
import net.kyori.adventure.util.Services
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.ZonedDateTime
import java.util.*

@AutoService(RpPlayerManager::class)
class RpPlayerManagerImpl : RpPlayerManager, Services.Fallback {

    private val _players = mutableObjectSetOf<RpPlayer>()
    override val players get() = _players.freeze()

    suspend fun findOrCreate(uuid: UUID) = newSuspendedTransaction(Dispatchers.IO) {
        RpPlayerModel.find { RpPlayerTable.uuid eq uuid }.singleOrNull() ?: RpPlayerModel.new {
            this.uuid = uuid
            this.createdAt = ZonedDateTime.now()
            this.updatedAt = ZonedDateTime.now()
        }
    }

    suspend fun updateUsername(
        rpPlayer: RpPlayerImpl,
        username: String
    ): Unit = newSuspendedTransaction(Dispatchers.IO) {
        rpPlayer.username = username
        rpPlayer.updatedAt = ZonedDateTime.now()

        RpPlayerModel.findSingleByAndUpdate((RpPlayerTable.uuid eq rpPlayer.uuid)) {
            it.username = username
            it.updatedAt = rpPlayer.updatedAt
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

    fun disconnectPlayer(player: RpPlayer) = _players.removeIf { it.uuid == player.uuid }

    override suspend fun get(uuid: UUID): RpPlayer {
        val cacheHit = players.firstOrNull { it.uuid == uuid }

        if (cacheHit != null) {
            return cacheHit
        }

        return findOrCreate(uuid).toApi().also {
            _players.add(it)
        }
    }
}

val rpPlayerManagerImpl get() = RpPlayerManager.INSTANCE as RpPlayerManagerImpl
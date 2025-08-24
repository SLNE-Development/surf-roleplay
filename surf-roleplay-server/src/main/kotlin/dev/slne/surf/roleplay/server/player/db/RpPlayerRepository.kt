package dev.slne.surf.roleplay.server.player.db

import dev.slne.surf.cloud.api.common.util.singleOrNullOrThrow
import dev.slne.surf.cloud.api.server.plugin.CoroutineTransactional
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime
import java.util.*

@CoroutineTransactional
@Repository
class RpPlayerRepository {

    suspend fun findPlayerId(uuid: UUID) = RpPlayerTable.select(RpPlayerTable.id)
        .where { RpPlayerTable.uuid eq uuid }
        .singleOrNullOrThrow()
        ?.get(RpPlayerTable.id)

    suspend fun findOrCreateModel(uuid: UUID) =
        RpPlayerModel.find { RpPlayerTable.uuid eq uuid }.singleOrNullOrThrow()
            ?: RpPlayerModel.new {
                this.uuid = uuid
                this.createdAt = ZonedDateTime.now()
                this.updatedAt = ZonedDateTime.now()
            }

    suspend fun updateUsername(uuid: UUID, username: String) =
        RpPlayerModel.findSingleByAndUpdate(RpPlayerTable.uuid eq uuid) {
            it.username = username
        }
}
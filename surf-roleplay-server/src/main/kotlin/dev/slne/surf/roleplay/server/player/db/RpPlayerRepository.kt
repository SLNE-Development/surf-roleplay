package dev.slne.surf.roleplay.server.player.db

import dev.slne.surf.cloud.api.common.util.singleOrNullOrThrow
import dev.slne.surf.cloud.api.server.plugin.CoroutineTransactional
import dev.slne.surf.roleplay.core.common.player.identity.RpIdentityType
import dev.slne.surf.roleplay.core.common.player.identity.create.RpIdentityCreationData
import dev.slne.surf.roleplay.core.common.player.identity.update.RpIdentityUpdateData
import dev.slne.surf.roleplay.core.player.db.RpPlayerModel
import dev.slne.surf.roleplay.server.player.identity.db.impl.police.RpPlayerPoliceIdentityModel
import dev.slne.surf.roleplay.server.player.identity.db.RpPlayerIdentityBaseModel
import dev.slne.surf.roleplay.server.player.identity.db.impl.civilian.RpPlayerCivilianIdentityModel
import dev.slne.surf.roleplay.server.player.identity.db.impl.rescueservice.RpPlayerRescueServiceIdentityModel
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

    private suspend fun findIdentityModelByType(
        uuid: UUID,
        type: RpIdentityType
    ): RpPlayerIdentityBaseModel<*>? {
        val playerId = findPlayerId(uuid) ?: return null
        val modelClass = when (type) {
            RpIdentityType.CIVILIAN -> RpPlayerCivilianIdentityModel
            RpIdentityType.POLICE -> RpPlayerPoliceIdentityModel
            RpIdentityType.RESCUE_SERVICE -> RpPlayerRescueServiceIdentityModel
        }

        return modelClass.find { modelClass.identityTable.player eq playerId }.singleOrNullOrThrow()
    }

    suspend fun createIdentity(uuid: UUID, creationData: RpIdentityCreationData) {
        val playerModel = findOrCreateModel(uuid)

        fun RpPlayerIdentityBaseModel<*>.applyCommonData() {
            this.player = playerModel
            this.firstName = creationData.firstName
            this.lastName = creationData.lastName
            this.dateOfBirth = creationData.dateOfBirth
        }

        when (creationData) {
            is RpIdentityCreationData.Civilian -> RpPlayerCivilianIdentityModel.new {
                applyCommonData()
            }

            is RpIdentityCreationData.Police -> RpPlayerPoliceIdentityModel.new {
                applyCommonData()
                this.badgeNumber = creationData.badgeNumber
                this.rank = creationData.rank
            }

            is RpIdentityCreationData.RescueService -> RpPlayerRescueServiceIdentityModel.new {
                applyCommonData()
                this.rank = creationData.rank
            }
        }
    }

    suspend fun updateIdentity(uuid: UUID, updateData: RpIdentityUpdateData): Boolean {
        val existingModel = findIdentityModelByType(uuid, updateData.identityType) ?: return false

        fun RpPlayerIdentityBaseModel<*>.applyCommonUpdate() {
            updateData.firstName?.let { this.firstName = it }
            updateData.lastName?.let { this.lastName = it }
            updateData.dateOfBirth?.let { this.dateOfBirth = it }
        }

        when (existingModel) {
            is RpPlayerCivilianIdentityModel -> {
                if (updateData !is RpIdentityUpdateData.Civilian) return false
                existingModel.applyCommonUpdate()
            }

            is RpPlayerPoliceIdentityModel -> {
                if (updateData !is RpIdentityUpdateData.Police) return false
                existingModel.applyCommonUpdate()
                updateData.badgeNumber?.let { existingModel.badgeNumber = it }
                updateData.rank?.let { existingModel.rank = it }
            }

            is RpPlayerRescueServiceIdentityModel -> {
                if (updateData !is RpIdentityUpdateData.RescueService) return false
                existingModel.applyCommonUpdate()
                updateData.rank?.let { existingModel.rank = it }
            }

            else -> error("Unsupported identity model type: ${existingModel::class.qualifiedName}")
        }

        return true
    }
}
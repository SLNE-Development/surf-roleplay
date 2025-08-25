package dev.slne.surf.roleplay.server.player.identity.db

import dev.slne.surf.cloud.api.common.util.singleOrNullOrThrow
import dev.slne.surf.roleplay.core.common.player.identity.NetworkIdentity
import dev.slne.surf.roleplay.core.common.player.identity.RpIdentityType
import dev.slne.surf.roleplay.server.player.db.RpPlayerRepository
import dev.slne.surf.roleplay.server.player.identity.db.impl.civilian.RpPlayerCivilianIdentityModel
import dev.slne.surf.roleplay.server.player.identity.db.impl.police.RpPlayerPoliceIdentityModel
import dev.slne.surf.roleplay.server.player.identity.db.impl.rescueservice.RpPlayerRescueServiceIdentityModel
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class RpIdentityRepository(private val rpPlayerRepository: RpPlayerRepository) {

    suspend fun findByUuid(uuid: UUID): List<NetworkIdentity> {
        val player = rpPlayerRepository.findOrCreateModel(uuid)

        return listOfNotNull(
            player.civilianIdentity?.toNetwork(),
            player.policeIdentity?.toNetwork(),
            player.rescueServiceIdentity?.toNetwork()
        )
    }

    private suspend fun findIdentityModelByType(
        uuid: UUID,
        type: RpIdentityType
    ): RpPlayerIdentityBaseModel? {
        val playerId = rpPlayerRepository.findPlayerId(uuid) ?: return null
        val modelClass = when (type) {
            RpIdentityType.CIVILIAN -> RpPlayerCivilianIdentityModel
            RpIdentityType.POLICE -> RpPlayerPoliceIdentityModel
            RpIdentityType.RESCUE_SERVICE -> RpPlayerRescueServiceIdentityModel
        }

        return modelClass.find { modelClass.identityTable.player eq playerId }.singleOrNullOrThrow()
    }

    suspend fun createIdentity(uuid: UUID, identity: NetworkIdentity): NetworkIdentity {
        val playerModel = rpPlayerRepository.findOrCreateModel(uuid)

        fun RpPlayerIdentityBaseModel.applyCommonData() {
            this.player = playerModel
            this.firstName = identity.firstName
            this.lastName = identity.lastName
            this.dateOfBirth = identity.dateOfBirth
        }

        val created = when (identity) {
            is NetworkIdentity.Civilian -> RpPlayerCivilianIdentityModel.new {
                applyCommonData()
            }

            is NetworkIdentity.Police -> RpPlayerPoliceIdentityModel.new {
                applyCommonData()
                this.badgeNumber = identity.badgeNumber
                this.rank = identity.rank
            }

            is NetworkIdentity.RescueService -> RpPlayerRescueServiceIdentityModel.new {
                applyCommonData()
                this.rank = identity.rank
            }
        }

        return created.toNetwork()
    }

    suspend fun updateOrCreateIdentity(uuid: UUID, identity: NetworkIdentity): NetworkIdentity {
        val existingModel =
            findIdentityModelByType(uuid, identity.type) ?: return createIdentity(uuid, identity)

        fun RpPlayerIdentityBaseModel.applyCommonUpdate() = apply {
            this.firstName = identity.firstName
            this.lastName = identity.lastName
            this.dateOfBirth = identity.dateOfBirth
        }

        val updated = when (existingModel) {
            is RpPlayerCivilianIdentityModel -> {
                require(identity is NetworkIdentity.Civilian) { "Invalid identity type: ${identity::class.qualifiedName}. Expected: ${NetworkIdentity.Civilian::class.qualifiedName}" }
                existingModel.applyCommonUpdate()
            }

            is RpPlayerPoliceIdentityModel -> {
                require(identity is NetworkIdentity.Police) { "Invalid identity type: ${identity::class.qualifiedName}. Expected: ${NetworkIdentity.Police::class.qualifiedName}" }
                existingModel.apply {
                    applyCommonUpdate()
                    this.badgeNumber = identity.badgeNumber
                    this.rank = identity.rank
                }
            }

            is RpPlayerRescueServiceIdentityModel -> {
                require(identity is NetworkIdentity.RescueService) { "Invalid identity type: ${identity::class.qualifiedName}. Expected: ${NetworkIdentity.RescueService::class.qualifiedName}" }
                existingModel.apply {
                    applyCommonUpdate()
                    this.rank = identity.rank
                }
            }

            else -> error("Unsupported identity type: ${identity::class.qualifiedName}")
        }

        return updated.toNetwork()
    }
}
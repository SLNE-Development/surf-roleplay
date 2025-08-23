package dev.slne.surf.roleplay.server.player

import dev.slne.surf.cloud.api.common.player.CloudPlayerManager
import dev.slne.surf.roleplay.core.common.player.RpPlayer
import dev.slne.surf.roleplay.core.common.player.RpPlayerManager
import dev.slne.surf.roleplay.core.common.player.identity.RpIdentity
import dev.slne.surf.roleplay.server.player.identity.db.impl.police.RpPlayerPoliceIdentityModel
import dev.slne.surf.roleplay.server.player.RpPlayerService
import dev.slne.surf.roleplay.server.player.identity.db.impl.civilian.RpPlayerCivilianIdentityModel
import dev.slne.surf.roleplay.server.player.identity.db.impl.rescueservice.RpPlayerRescueServiceIdentityModel
import dev.slne.surf.surfapi.core.api.util.getCallerClass
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.springframework.stereotype.Component
import java.time.ZonedDateTime
import java.util.*

@Component
class RpPlayerManagerImpl(private val rpPlayerService: RpPlayerService) : RpPlayerManager() {

    override fun createPlayer(uuid: UUID) = ServerRpPlayer(uuid)
    override fun onlineUuids() = CloudPlayerManager.getOnlinePlayers().uuidSnapshot()

    override suspend fun fetchIdentities(uuid: UUID): Set<RpIdentity> {
        rpPlayerService
    }

//    suspend fun updateUsername(
//        rpPlayer: RpPlayerImpl,
//        username: String
//    ): Unit = newSuspendedTransaction(Dispatchers.IO) {
//        rpPlayer.username = username
//        rpPlayer.updatedAt = ZonedDateTime.now()
//
//        RpPlayerModel.findSingleByAndUpdate((RpPlayerTable.uuid eq rpPlayer.uuid)) {
//            it.username = username
//            it.updatedAt = rpPlayer.updatedAt
//        }
//    }

    @Suppress("UNCHECKED_CAST")
    suspend fun <T : RpIdentity> createIdentity(
        player: RpPlayerImpl,
        identity: T
    ): T = newSuspendedTransaction(Dispatchers.IO) {
        val rpPlayerModel = rpPlayerManagerImpl.findOrCreate(player.uuid)

        val identity = when (identity) {
            is RpIdentity.CivilianIdentity -> {
                RpPlayerCivilianIdentityModel.new {
                    this.player = rpPlayerModel
                    firstName = identity.firstName
                    lastName = identity.lastName
                    dateOfBirth = identity.dateOfBirth
                    createdAt = identity.createdAt
                    updatedAt = identity.updatedAt
                }
            }

            is RpIdentity.PoliceIdentity -> {
                RpPlayerPoliceIdentityModel.new {
                    this.player = rpPlayerModel
                    firstName = identity.firstName
                    lastName = identity.lastName
                    dateOfBirth = identity.dateOfBirth
                    badgeNumber = identity.badgeNumber
                    rank = identity.rank
                    createdAt = identity.createdAt
                    updatedAt = identity.updatedAt
                }
            }

            is RpIdentity.RescueServiceIdentity -> {
                RpPlayerRescueServiceIdentityModel.new {
                    this.player = rpPlayerModel
                    firstName = identity.firstName
                    lastName = identity.lastName
                    dateOfBirth = identity.dateOfBirth
                    rank = identity.rank
                    createdAt = identity.createdAt
                    updatedAt = identity.updatedAt
                }
            }

            else -> error("Unsupported identity type: ${identity::class.qualifiedName}")
        }

        val api = identity.toApi(player)
        player.addIdentity(api)

        api as T
    }

    suspend fun <T : RpIdentity> createOrUpdateIdentity(
        player: RpPlayerImpl,
        identity: T
    ): T = newSuspendedTransaction(Dispatchers.IO) {
        val entity = findIdentityByType(player, identity)

        if (entity == null) {
            return@newSuspendedTransaction createIdentity(player, identity)
        }

        updateIdentity(player, identity)
    }

    suspend fun <T : RpIdentity> updateIdentity(
        player: RpPlayerImpl,
        identity: T,
    ): T = newSuspendedTransaction(Dispatchers.IO) {
        val entity = findIdentityByType(player, identity)

        identity.updatedAt = ZonedDateTime.now()

        when (entity) {
            is RpPlayerCivilianIdentityModel -> {
                entity.firstName = identity.firstName
                entity.lastName = identity.lastName
                entity.dateOfBirth = identity.dateOfBirth
                entity.updatedAt = identity.updatedAt
            }

            is RpPlayerPoliceIdentityModel -> {
                identity as RpIdentity.PoliceIdentity

                entity.firstName = identity.firstName
                entity.lastName = identity.lastName
                entity.dateOfBirth = identity.dateOfBirth
                entity.badgeNumber = identity.badgeNumber
                entity.rank = identity.rank
                entity.updatedAt = identity.updatedAt
            }

            is RpPlayerRescueServiceIdentityModel -> {
                identity as RpIdentity.RescueServiceIdentity

                entity.firstName = identity.firstName
                entity.lastName = identity.lastName
                entity.dateOfBirth = identity.dateOfBirth
                entity.rank = identity.rank
                entity.updatedAt = identity.updatedAt
            }

            else -> error("Unsupported identity type: ${identity::class.qualifiedName}")
        }

        identity
    }

    override suspend fun get(uuid: UUID): RpPlayer {
        println("Caller: " + getCallerClass(1))
        return cache.get(uuid)
    }
}

val rpPlayerManagerImpl get() = RpPlayerManager.INSTANCE as RpPlayerManagerImpl
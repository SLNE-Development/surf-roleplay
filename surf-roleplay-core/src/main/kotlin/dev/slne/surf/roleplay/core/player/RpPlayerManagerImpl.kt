package dev.slne.surf.roleplay.core.player

import com.github.benmanes.caffeine.cache.Caffeine
import com.google.auto.service.AutoService
import com.sksamuel.aedile.core.asLoadingCache
import com.sksamuel.aedile.core.withRemovalListener
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.RpPlayerManager
import dev.slne.surf.roleplay.api.player.identity.RpIdentity
import dev.slne.surf.roleplay.core.player.db.RpPlayerModel
import dev.slne.surf.roleplay.core.player.db.RpPlayerTable
import dev.slne.surf.roleplay.core.player.identity.db.impl.civilian.RpPlayerCivilianIdentityModel
import dev.slne.surf.roleplay.core.player.identity.db.impl.civilian.RpPlayerCivilianIdentityTable
import dev.slne.surf.roleplay.core.player.identity.db.impl.police.RpPlayerPoliceIdentityModel
import dev.slne.surf.roleplay.core.player.identity.db.impl.police.RpPlayerPoliceIdentityTable
import dev.slne.surf.roleplay.core.player.identity.db.impl.rescueservice.RpPlayerRescueServiceIdentityModel
import dev.slne.surf.roleplay.core.player.identity.db.impl.rescueservice.RpPlayerRescueServiceIdentityTable
import dev.slne.surf.surfapi.core.api.util.getCallerClass
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import kotlinx.coroutines.Dispatchers
import net.kyori.adventure.util.Services
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.ZonedDateTime
import java.util.*

@AutoService(RpPlayerManager::class)
class RpPlayerManagerImpl : RpPlayerManager, Services.Fallback {

    private val cache = Caffeine.newBuilder()
        .withRemovalListener { key, _, cause ->
            println("Removing RpPlayer from cache: $key due to $cause")
        }
        .asLoadingCache<UUID, RpPlayer> {
            println("Loading RpPlayer from database: $it")
            findOrCreate(it).toApi()
        }

    override val players
        get() = cache.underlying().asMap().values.mapNotNull { it.getNow(null) }.toObjectSet()

    fun onDisconnect(player: RpPlayer) = cache.invalidate(player.uuid)

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

    private suspend fun <T : RpIdentity> findIdentityByType(
        rpPlayer: RpPlayerImpl,
        identity: T
    ) = newSuspendedTransaction(Dispatchers.IO) {
        val rpPlayerModel = rpPlayerManagerImpl.findOrCreate(rpPlayer.uuid)

        when (identity) {
            is RpIdentity.CivilianIdentity -> {
                RpPlayerCivilianIdentityModel.find {
                    (RpPlayerCivilianIdentityTable.player eq rpPlayerModel.id)
                }.singleOrNull()
            }

            is RpIdentity.PoliceIdentity -> {
                RpPlayerPoliceIdentityModel.find {
                    (RpPlayerPoliceIdentityTable.player eq rpPlayerModel.id)
                }.singleOrNull()
            }

            is RpIdentity.RescueServiceIdentity -> {
                RpPlayerRescueServiceIdentityModel.find {
                    (RpPlayerRescueServiceIdentityTable.player eq rpPlayerModel.id)
                }.singleOrNull()
            }

            else -> error("Unsupported identity type: ${identity::class.qualifiedName}")
        }
    }

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
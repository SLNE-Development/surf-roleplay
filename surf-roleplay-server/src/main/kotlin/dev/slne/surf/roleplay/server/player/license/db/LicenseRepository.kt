package dev.slne.surf.roleplay.server.player.license.db

import dev.slne.surf.cloud.api.server.plugin.CoroutineTransactional
import dev.slne.surf.roleplay.core.common.player.identity.RpIdentityType
import dev.slne.surf.roleplay.core.common.player.license.NetworkIdentityLicense
import dev.slne.surf.roleplay.server.player.db.RpPlayerRepository
import net.kyori.adventure.key.Key
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime
import java.util.*

@CoroutineTransactional
@Repository
class LicenseRepository(private val rpPlayerRepository: RpPlayerRepository) {

    suspend fun findLicensesForIdentity(
        uuid: UUID,
        type: RpIdentityType
    ): List<NetworkIdentityLicense> {
        val playerId = rpPlayerRepository.findPlayerId(uuid) ?: return emptyList()
        return IdentityLicenseModel.find { (IdentityLicenseTable.player eq playerId) and (IdentityLicenseTable.identity eq type) }
            .map { it.toNetwork() }
    }

    suspend fun createLicense(
        uuid: UUID,
        identityType: RpIdentityType,
        licenseKey: Key,
        expiresAt: ZonedDateTime?,
        createdAt: ZonedDateTime
    ) {
        val playerModel = rpPlayerRepository.findOrCreateModel(uuid)

        IdentityLicenseModel.new {
            this.player = playerModel
            this.identity = identityType
            this.license = licenseKey
            this.expiresAt = expiresAt
            this.createdAt = createdAt
        }
    }

    suspend fun deleteLicense(uuid: UUID, identityType: RpIdentityType, licenseKey: Key): Int {
        val playerId = rpPlayerRepository.findPlayerId(uuid) ?: return 0
        val deleted = IdentityLicenseTable.deleteWhere {
            (player eq playerId) and (identity eq identityType) and (license eq licenseKey)
        }
        return deleted
    }
}
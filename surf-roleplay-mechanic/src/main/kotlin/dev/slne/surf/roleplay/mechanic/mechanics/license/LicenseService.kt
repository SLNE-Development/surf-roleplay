package dev.slne.surf.roleplay.mechanic.mechanics.license

import dev.slne.surf.roleplay.api.mechanic.license.License
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.core.player.rpPlayerManagerImpl
import dev.slne.surf.roleplay.mechanic.mechanics.license.db.PlayerLicenseModel
import dev.slne.surf.roleplay.mechanic.mechanics.license.db.PlayerLicenseTable
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.ZonedDateTime

object LicenseService {

    suspend fun createPlayerLicense(
        license: License,
        player: RpPlayer
    ) = newSuspendedTransaction(Dispatchers.IO) {
        val rpPlayerModel = rpPlayerManagerImpl.findOrCreate(player.uuid)

        PlayerLicenseModel.new {
            this.rpPlayer = rpPlayerModel
            this.license = license.key.asString()
            this.expiresAt = license.expiresIn?.let {
                ZonedDateTime.now().plusSeconds(it.inWholeSeconds)
            }
            this.createdAt = ZonedDateTime.now()
        }.toApi()
    }

    suspend fun removePlayerLicense(
        rpPlayer: RpPlayer,
        license: License
    ) = newSuspendedTransaction(Dispatchers.IO) {
        val rpPlayerModel = rpPlayerManagerImpl.findOrCreate(rpPlayer.uuid)
        val playerLicense = PlayerLicenseModel.find {
            (PlayerLicenseTable.rpPlayer eq rpPlayerModel.id) and
                    (PlayerLicenseTable.license eq license.key.asString())
        }.firstOrNull() ?: return@newSuspendedTransaction false

        playerLicense.delete()
        
        true
    }

    suspend fun getAllExpiredLicenses() = newSuspendedTransaction(Dispatchers.IO) {
        val licenses = PlayerLicenseModel.find {
            PlayerLicenseTable.expiresAt.isNotNull() and
                    (PlayerLicenseTable.expiresAt lessEq ZonedDateTime.now())
        }

        licenses.map { it.toApi() }.toObjectSet()
    }

}
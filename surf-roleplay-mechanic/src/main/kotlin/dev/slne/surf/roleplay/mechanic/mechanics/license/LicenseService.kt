package dev.slne.surf.roleplay.mechanic.mechanics.license

import dev.slne.surf.roleplay.api.mechanic.license.License
import dev.slne.surf.roleplay.api.mechanic.license.PlayerLicense
import dev.slne.surf.roleplay.api.mechanic.license.player.LicensePlayer
import dev.slne.surf.roleplay.api.mechanic.license.utils.LicenseRemovedReason
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
        playerLicense: PlayerLicense
    ) = newSuspendedTransaction(Dispatchers.IO) {
        val (player, license, expiresAt) = playerLicense
        val rpPlayerModel = rpPlayerManagerImpl.findOrCreate(player.uuid)

        PlayerLicenseModel.new {
            this.rpPlayer = rpPlayerModel
            this.license = license.key.asString()
            this.expiresAt = expiresAt
            this.createdAt = ZonedDateTime.now()
        }.toApi()
    }

    suspend fun createPlayerLicense(
        license: License,
        player: RpPlayer
    ) = createPlayerLicense(
        PlayerLicense(
            player = player,
            license = license,
            expiresAt = license.expiresIn?.let {
                ZonedDateTime.now().plusSeconds(it.inWholeSeconds)
            }
        )
    )

    suspend fun confiscateLicense(
        player: LicensePlayer,
        license: License,
        confiscatedBy: RpPlayer,
        confiscatedReason: String
    ) = newSuspendedTransaction(Dispatchers.IO) {
        val parentLicense =
            player.getLicense(license.javaClass) ?: return@newSuspendedTransaction false
        
        val parentResult = player.removeLicense(
            license,
            LicenseRemovedReason.Confiscated(confiscatedBy, confiscatedReason)
        )

        val childrenResults = license.children.map { childLicense ->
            if (player.hasLicense(childLicense)) {
                player.removeLicense(
                    childLicense,
                    LicenseRemovedReason.ConfiscatedChild(
                        parentLicense,
                        confiscatedBy,
                        confiscatedReason
                    )
                )
                return@map true
            }

            false
        }

        parentResult && childrenResults.all { it }
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
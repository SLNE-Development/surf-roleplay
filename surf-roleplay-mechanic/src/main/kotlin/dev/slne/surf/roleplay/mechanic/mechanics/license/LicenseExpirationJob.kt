package dev.slne.surf.roleplay.mechanic.mechanics.license

import dev.slne.surf.roleplay.api.coroutine.RpJob
import dev.slne.surf.roleplay.api.mechanic.license.utils.LicenseRemovedReason
import dev.slne.surf.roleplay.mechanic.mechanics.license.player.licensePlayer
import kotlin.time.Duration.Companion.seconds

object LicenseExpirationJob : RpJob("LicenseExpirationJob", 1.seconds) {
    override suspend fun tick() {
        val expiredLicenses = LicenseService.getAllExpiredLicenses()

        expiredLicenses.forEach { playerLicense ->
            playerLicense.player.licensePlayer()
                .removeLicense(playerLicense.license, LicenseRemovedReason.Expired)
        }
    }
}
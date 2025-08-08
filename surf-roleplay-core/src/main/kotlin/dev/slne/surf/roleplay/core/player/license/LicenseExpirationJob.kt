package dev.slne.surf.roleplay.core.player.license

import dev.slne.surf.roleplay.api.coroutine.RpJob
import dev.slne.surf.roleplay.api.player.license.utils.LicenseRemovedReason
import kotlin.time.Duration.Companion.seconds

object LicenseExpirationJob : RpJob("LicenseExpirationJob", 1.seconds) {
    override suspend fun tick() {
        val expiredLicenses = licenseServiceImpl.getAllExpiredLicenses()

        expiredLicenses.forEach { identityLicense ->
            identityLicense.identity.removeLicense(
                identityLicense.license,
                LicenseRemovedReason.Expired
            )
        }
    }
}
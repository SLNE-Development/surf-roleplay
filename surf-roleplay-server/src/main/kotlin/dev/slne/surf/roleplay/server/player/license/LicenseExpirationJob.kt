@file:OptIn(InternalRoleplayApi::class)

package dev.slne.surf.roleplay.server.player.license

import dev.slne.surf.roleplay.api.common.player.license.InternalLicenseBridge
import dev.slne.surf.roleplay.api.common.player.license.utils.LicenseRemovedReason
import dev.slne.surf.roleplay.api.common.util.InternalRoleplayApi
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class LicenseExpirationJob {

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.SECONDS)
    suspend fun tick() {
        val licenses = InternalLicenseBridge.instance.getAllExpiredLicenses()

        licenses.forEach { (identity, expiredLicenses) ->
            expiredLicenses.forEach { expiredLicense ->
                identity.removeLicense(expiredLicense.license, LicenseRemovedReason.Expired)
            }
        }
    }
}
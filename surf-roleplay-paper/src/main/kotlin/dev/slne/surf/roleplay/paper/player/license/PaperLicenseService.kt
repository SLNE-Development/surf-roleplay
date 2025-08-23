package dev.slne.surf.roleplay.paper.player.license

import dev.slne.surf.roleplay.core.common.player.RpPlayer
import dev.slne.surf.roleplay.core.common.player.RpPlayerManager
import dev.slne.surf.roleplay.paper.player.identity.RpIdentity
import dev.slne.surf.roleplay.paper.player.license.utils.LicenseRemovedReason
import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.ObjectSet
import org.springframework.beans.factory.ObjectProvider
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class PaperLicenseService(
    private val licenses: ObjectProvider<License>,
    private val rpPlayerManager: RpPlayerManager
) {
    fun all() = licenses.asSequence()

    suspend fun createLicense(identityLicense: IdentityLicense): IdentityLicense {

    }

    suspend fun removeLicense(
        identity: RpIdentity,
        license: License
    ): Boolean {

    }

    suspend fun confiscateLicense(
        identity: RpIdentity,
        license: License,
        confiscatedBy: RpPlayer,
        confiscatedReason: String
    ): Boolean {

    }

    fun getAllExpiredLicenses(): Object2ObjectMap<RpIdentity, ObjectSet<IdentityLicense>> {
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.SECONDS)
    fun removeExpiredLicenses() {
        val expired = getAllExpiredLicenses()

        for ((identity, expiredLicenses) in expired) {
            for (license in expiredLicenses) {
                identity.removeLicense(license.license, LicenseRemovedReason.Expired)
            }
        }
    }
}
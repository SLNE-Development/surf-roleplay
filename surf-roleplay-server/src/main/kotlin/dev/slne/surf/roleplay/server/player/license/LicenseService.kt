package dev.slne.surf.roleplay.server.player.license

import dev.slne.surf.roleplay.core.common.player.identity.RpIdentityType
import dev.slne.surf.roleplay.core.common.player.license.NetworkIdentityLicense
import dev.slne.surf.roleplay.server.player.license.db.LicenseRepository
import net.kyori.adventure.key.Key
import org.springframework.stereotype.Service
import java.util.*

@Service
class LicenseService(private val licenseRepository: LicenseRepository) {

    suspend fun createLicense(license: NetworkIdentityLicense) {
        licenseRepository.createLicense(
            license.owner,
            license.identityType,
            license.licenseKey,
            license.expiresAt,
            license.createdAt
        )
    }

    suspend fun deleteLicense(uuid: UUID, identityType: RpIdentityType, licenseKey: Key): Boolean {
        return licenseRepository.deleteLicense(uuid, identityType, licenseKey) > 0
    }
}
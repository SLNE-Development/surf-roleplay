package dev.slne.surf.roleplay.paper.player.license

import dev.slne.surf.cloud.api.client.netty.packet.awaitOrThrow
import dev.slne.surf.roleplay.core.common.network.packets.ServerboundCreateLicensePacket
import dev.slne.surf.roleplay.core.common.network.packets.ServerboundDeleteLicensePacket
import dev.slne.surf.roleplay.core.common.player.RpPlayer
import dev.slne.surf.roleplay.paper.player.PaperRpPlayerManager
import dev.slne.surf.roleplay.paper.player.identity.RpIdentity
import dev.slne.surf.roleplay.paper.player.license.utils.LicenseRemovedReason
import dev.slne.surf.surfapi.core.api.util.mapAsync
import org.springframework.beans.factory.ObjectProvider
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class PaperLicenseService(
    private val licenses: ObjectProvider<License>,
    private val rpPlayerManager: PaperRpPlayerManager
) {
    fun all() = licenses.asSequence()

    suspend fun createLicense(identityLicense: IdentityLicense): IdentityLicense {
        ServerboundCreateLicensePacket(identityLicense.toNetwork()).awaitOrThrow()
        return identityLicense
    }

    suspend fun removeLicense(
        identity: RpIdentity,
        license: License
    ): Boolean = ServerboundDeleteLicensePacket(identity.uuid, identity.type, license.key)
        .awaitOrThrow()

    suspend fun confiscateLicense(
        identity: RpIdentity,
        license: License,
        confiscatedBy: RpPlayer,
        confiscatedReason: String
    ): Boolean {
        val player = identity.player
        val parentLicense =
            player.getIdentity(identity.type)?.getLicense(license::class.java) ?: return false

        val parentResult = player.removeLicense(
            license,
            LicenseRemovedReason.Confiscated(confiscatedBy, confiscatedReason)
        )

        val childrenResults = license.children.mapAsync { childLicense ->
            if (player.hasLicense(childLicense)) {
                player.removeLicense(
                    childLicense,
                    LicenseRemovedReason.ConfiscatedChild(
                        parentLicense,
                        confiscatedBy,
                        confiscatedReason
                    )
                )
            } else {
                true
            }
        }

        return parentResult && childrenResults.all { it }
    }

    fun getAllExpiredLicenses() = rpPlayerManager.players
        .mapNotNull { it.activeIdentity }
        .flatMap { id -> id.getExpiredLicenses().map { license -> id to license } }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.SECONDS)
    suspend fun removeExpiredLicenses() {
        getAllExpiredLicenses()
            .mapAsync { (identity, license) ->
                identity.removeLicense(license.license, LicenseRemovedReason.Expired)
            }
    }
}
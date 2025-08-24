package dev.slne.surf.roleplay.server.player.license

import dev.slne.surf.cloud.api.common.meta.SurfNettyPacketHandler
import dev.slne.surf.cloud.api.common.netty.network.protocol.respond
import dev.slne.surf.roleplay.core.common.network.packets.ServerboundCreateLicensePacket
import dev.slne.surf.roleplay.core.common.network.packets.ServerboundDeleteLicensePacket
import org.springframework.stereotype.Component

@Suppress("unused")
@Component
class LicensePacketListener(private val licenseService: LicenseService) {

    @SurfNettyPacketHandler
    suspend fun handleCreateLicense(packet: ServerboundCreateLicensePacket) {
        licenseService.createLicense(packet.license)
        packet.respond(true)
    }

    @SurfNettyPacketHandler
    suspend fun handleDeleteLicense(packet: ServerboundDeleteLicensePacket) {
        val deleted = licenseService.deleteLicense(
            packet.uuid,
            packet.identityType,
            packet.licenseKey
        )

        packet.respond(deleted)
    }
}
package dev.slne.surf.roleplay.api.mechanic.license.event

import dev.slne.surf.roleplay.api.events.CancellableRpEvent
import dev.slne.surf.roleplay.api.mechanic.license.PlayerLicense
import dev.slne.surf.roleplay.api.player.RpPlayer

class PlayerLicenseAddedEvent(
    val player: RpPlayer,
    val license: PlayerLicense
) : CancellableRpEvent() {
    var cancelReason: String? = null
}
package dev.slne.surf.roleplay.api.mechanic.license.event

import dev.slne.surf.roleplay.api.events.CancellableEvent
import dev.slne.surf.roleplay.api.mechanic.license.License
import dev.slne.surf.roleplay.api.player.RpPlayer

class PlayerLicenseAddedEvent(
    val player: RpPlayer,
    val license: License,
    async: Boolean = false
) : CancellableEvent(async) {
    var cancelReason: String? = null
}
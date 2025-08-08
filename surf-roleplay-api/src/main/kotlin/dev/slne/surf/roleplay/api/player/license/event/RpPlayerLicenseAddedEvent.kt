package dev.slne.surf.roleplay.api.player.license.event

import dev.slne.surf.roleplay.api.events.CancellableRpEvent
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.identity.RpIdentity
import dev.slne.surf.roleplay.api.player.license.IdentityLicense

class RpPlayerLicenseAddedEvent(
    val player: RpPlayer,
    val identity: RpIdentity,
    val license: IdentityLicense
) : CancellableRpEvent() {
    var cancelReason: String? = null
}
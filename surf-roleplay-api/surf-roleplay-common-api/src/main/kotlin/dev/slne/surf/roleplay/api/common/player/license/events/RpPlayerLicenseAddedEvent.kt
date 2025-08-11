package dev.slne.surf.roleplay.api.common.player.license.events

import dev.slne.surf.cloud.api.common.event.CancellableCloudEvent
import dev.slne.surf.roleplay.api.common.player.RpPlayer
import dev.slne.surf.roleplay.api.common.player.identity.RpIdentity
import dev.slne.surf.roleplay.api.common.player.license.IdentityLicense

class RpPlayerLicenseAddedEvent(
    source: Any,
    val player: RpPlayer,
    val identity: RpIdentity,
    val license: IdentityLicense
) : CancellableCloudEvent(source) {
    var cancelReason: String? = null
}
package dev.slne.surf.roleplay.paper.player.license.events

import dev.slne.surf.cloud.api.common.event.CancellableCloudEvent
import dev.slne.surf.roleplay.core.common.player.RpPlayer
import dev.slne.surf.roleplay.paper.player.identity.RpIdentity
import dev.slne.surf.roleplay.paper.player.license.IdentityLicense
import dev.slne.surf.roleplay.paper.player.license.utils.LicenseRemovedReason

class RpPlayerLicenseRemovedEvent(
    source: Any,
    val player: RpPlayer,
    val identity: RpIdentity,
    val license: IdentityLicense,
    val reason: LicenseRemovedReason,
) : CancellableCloudEvent(source)
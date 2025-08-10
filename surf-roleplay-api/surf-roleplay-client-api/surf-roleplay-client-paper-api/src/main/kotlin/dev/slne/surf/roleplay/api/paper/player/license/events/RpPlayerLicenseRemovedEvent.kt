package dev.slne.surf.roleplay.api.paper.player.license.events

import dev.slne.surf.roleplay.api.common.player.RpPlayer
import dev.slne.surf.roleplay.api.common.player.identity.RpIdentity
import dev.slne.surf.roleplay.api.common.player.license.IdentityLicense
import dev.slne.surf.roleplay.api.common.player.license.utils.LicenseRemovedReason
import dev.slne.surf.roleplay.api.paper.events.CancellableRpEvent

class RpPlayerLicenseRemovedEvent(
    val player: RpPlayer,
    val identity: RpIdentity,
    val license: IdentityLicense,
    val reason: LicenseRemovedReason,
) : CancellableRpEvent()
package dev.slne.surf.roleplay.api.player.license.event

import dev.slne.surf.roleplay.api.events.CancellableRpEvent
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.identity.RpIdentity
import dev.slne.surf.roleplay.api.player.license.IdentityLicense
import dev.slne.surf.roleplay.api.player.license.utils.LicenseRemovedReason

class RpPlayerLicenseRemovedEvent(
    val player: RpPlayer,
    val identity: RpIdentity,
    val license: IdentityLicense,
    val reason: LicenseRemovedReason,
) : CancellableRpEvent()
package dev.slne.surf.roleplay.api.mechanic.license.event

import dev.slne.surf.roleplay.api.events.CancellableEvent
import dev.slne.surf.roleplay.api.mechanic.license.License
import dev.slne.surf.roleplay.api.mechanic.license.utils.LicenseRemovedReason
import dev.slne.surf.roleplay.api.player.RpPlayer

class PlayerLicenseRemovedEvent(
    val player: RpPlayer,
    val license: License,
    val reason: LicenseRemovedReason,
    async: Boolean = false
) : CancellableEvent(async)
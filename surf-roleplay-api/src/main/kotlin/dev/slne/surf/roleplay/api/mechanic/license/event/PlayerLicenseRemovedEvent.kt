package dev.slne.surf.roleplay.api.mechanic.license.event

import dev.slne.surf.roleplay.api.events.CancellableEvent
import dev.slne.surf.roleplay.api.mechanic.license.PlayerLicense
import dev.slne.surf.roleplay.api.mechanic.license.utils.LicenseRemovedReason
import dev.slne.surf.roleplay.api.player.RpPlayer
import org.bukkit.Bukkit

class PlayerLicenseRemovedEvent(
    val player: RpPlayer,
    val license: PlayerLicense,
    val reason: LicenseRemovedReason,
    async: Boolean = !Bukkit.isPrimaryThread()
) : CancellableEvent(async)
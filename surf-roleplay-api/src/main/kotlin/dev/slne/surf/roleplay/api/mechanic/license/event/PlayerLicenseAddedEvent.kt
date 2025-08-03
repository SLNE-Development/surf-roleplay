package dev.slne.surf.roleplay.api.mechanic.license.event

import dev.slne.surf.roleplay.api.events.CancellableEvent
import dev.slne.surf.roleplay.api.mechanic.license.PlayerLicense
import dev.slne.surf.roleplay.api.player.RpPlayer
import org.bukkit.Bukkit

class PlayerLicenseAddedEvent(
    val player: RpPlayer,
    val license: PlayerLicense,
    async: Boolean = !Bukkit.isPrimaryThread()
) : CancellableEvent(async) {
    var cancelReason: String? = null
}
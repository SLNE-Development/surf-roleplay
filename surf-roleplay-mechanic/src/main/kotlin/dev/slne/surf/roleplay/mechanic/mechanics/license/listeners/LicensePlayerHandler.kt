package dev.slne.surf.roleplay.mechanic.mechanics.license.listeners

import dev.slne.surf.roleplay.api.player.events.RpPlayerQuitEvent
import dev.slne.surf.roleplay.mechanic.mechanics.license.player.LicensePlayerManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object LicensePlayerHandler : Listener {
    @EventHandler
    fun onPlayerQuit(event: RpPlayerQuitEvent) {
        LicensePlayerManager.remove(event.rpPlayer.uuid)
    }
}
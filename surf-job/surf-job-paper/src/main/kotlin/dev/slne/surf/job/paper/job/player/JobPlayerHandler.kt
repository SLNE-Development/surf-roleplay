package dev.slne.surf.job.paper.job.player

import dev.slne.surf.roleplay.api.player.events.RpPlayerQuitEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

object JobPlayerHandler : Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun onRpPlayerQuit(event: RpPlayerQuitEvent) {
        JobPlayerService.remove(event.rpPlayer.uuid)
    }
}
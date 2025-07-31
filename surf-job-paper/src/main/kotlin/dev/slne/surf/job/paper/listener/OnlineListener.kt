package dev.slne.surf.job.paper.listener

import dev.slne.surf.job.paper.player.jobPlayerManagerImpl
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

object OnlineListener : Listener {

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        jobPlayerManagerImpl.remove(event.player.uniqueId)
    }

}
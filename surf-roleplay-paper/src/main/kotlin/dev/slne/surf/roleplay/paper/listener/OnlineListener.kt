package dev.slne.surf.roleplay.paper.listener

import dev.slne.surf.roleplay.paper.player.events.RpPlayerDeathEvent
import dev.slne.surf.roleplay.paper.player.events.RpPlayerJoinEvent
import dev.slne.surf.roleplay.paper.player.events.RpPlayerQuitEvent
import dev.slne.surf.roleplay.paper.player.rpPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.springframework.stereotype.Component

@Component
class OnlineListener : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player.rpPlayer
        RpPlayerJoinEvent(this, player).postAndForget()
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player.rpPlayer
        RpPlayerQuitEvent(this, player).postAndForget()
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val player = event.player.rpPlayer
        RpPlayerDeathEvent(this, player, event).postAndForget()
    }
}
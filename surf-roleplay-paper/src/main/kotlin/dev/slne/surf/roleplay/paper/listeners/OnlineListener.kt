package dev.slne.surf.roleplay.paper.listeners

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.player.RpPlayerManager
import dev.slne.surf.roleplay.api.player.events.RpPlayerJoinEvent
import dev.slne.surf.roleplay.api.player.events.RpPlayerQuitEvent
import dev.slne.surf.roleplay.paper.plugin
import dev.slne.surf.surfapi.bukkit.api.extensions.server
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object OnlineListener : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        plugin.launch {
            server.pluginManager.callEvent(
                RpPlayerJoinEvent(
                    RpPlayerManager[event.player.uniqueId],
                    true
                )
            )
        }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        plugin.launch {
            server.pluginManager.callEvent(
                RpPlayerQuitEvent(
                    RpPlayerManager[event.player.uniqueId],
                    true
                )
            )
        }
    }
}
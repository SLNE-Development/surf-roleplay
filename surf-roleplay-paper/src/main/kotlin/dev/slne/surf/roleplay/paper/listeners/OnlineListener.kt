package dev.slne.surf.roleplay.paper.listeners

import com.github.shynixn.mccoroutine.folia.globalRegionDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.events.RpPlayerDeathEvent
import dev.slne.surf.roleplay.api.player.events.RpPlayerJoinEvent
import dev.slne.surf.roleplay.api.player.events.RpPlayerQuitEvent
import dev.slne.surf.roleplay.core.player.rpPlayerManagerImpl
import dev.slne.surf.roleplay.paper.plugin
import kotlinx.coroutines.withContext
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object OnlineListener : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onJoin(event: PlayerJoinEvent) {
        plugin.launch {
            val player = RpPlayer[event.player.uniqueId]

            player.updateUsername(event.player.name)

            withContext(plugin.globalRegionDispatcher) {
                RpPlayerJoinEvent(player).callEvent()
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onQuit(event: PlayerQuitEvent) {
        plugin.launch {
            val player = RpPlayer[event.player.uniqueId]

            withContext(plugin.globalRegionDispatcher) {
                RpPlayerQuitEvent(player).callEvent()
            }

            rpPlayerManagerImpl.onDisconnect(player)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerDeath(event: PlayerDeathEvent) {
        plugin.launch {
            withContext(plugin.globalRegionDispatcher) {
                RpPlayerDeathEvent(RpPlayer[event.player.uniqueId], event).callEvent()
            }
        }
    }
}
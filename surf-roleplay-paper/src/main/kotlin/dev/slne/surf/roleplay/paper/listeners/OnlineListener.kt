package dev.slne.surf.roleplay.paper.listeners

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.job.api.job.JobRegistry
import dev.slne.surf.job.api.job.getJob
import dev.slne.surf.job.api.job.jobs.neutral.CitizenJob
import dev.slne.surf.job.api.player.changeJob
import dev.slne.surf.roleplay.api.player.RpPlayer
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
            val player = RpPlayer[event.player.uniqueId]

            server.pluginManager.callEvent(
                RpPlayerJoinEvent(player)
            )

            player.changeJob(JobRegistry.getJob<CitizenJob>())
        }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        plugin.launch {
            server.pluginManager.callEvent(
                RpPlayerQuitEvent(
                    RpPlayer[event.player.uniqueId],
                    true
                )
            )
        }
    }
}
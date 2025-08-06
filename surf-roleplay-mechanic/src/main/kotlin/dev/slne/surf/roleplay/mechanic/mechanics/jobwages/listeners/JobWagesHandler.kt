package dev.slne.surf.roleplay.mechanic.mechanics.jobwages.listeners

import com.github.shynixn.mccoroutine.folia.entityDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.mechanic.jobwages.event.PlayerPaycheckEvent
import dev.slne.surf.roleplay.api.player.events.RpPlayerQuitEvent
import dev.slne.surf.roleplay.mechanic.mechanics.jobwages.JobWagesJob
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.core.api.messages.adventure.playSound
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

object JobWagesHandler : Listener {

    @EventHandler
    fun onPlayerWageAdded(event: PlayerPaycheckEvent) {
        val bukkitPlayer = event.player.bukkitPlayer ?: return

        if (event.amount <= 0) {
            return
        }

        plugin.launch(plugin.entityDispatcher(bukkitPlayer)) {
            bukkitPlayer.sendText {
                appendPrefix()

                info("Du hast eine Überweisung in Höhe von ")
                variableValue(event.amount)
                variableKey(" € ")
                info("erhalten.")
            }
            bukkitPlayer.playSound {
                type(Sound.ENTITY_PLAYER_LEVELUP)
                volume(.75f)
                source(net.kyori.adventure.sound.Sound.Source.NEUTRAL)
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onRpPlayerQuit(event: RpPlayerQuitEvent) {
        JobWagesJob.playerDisconnect(event.rpPlayer)
    }

}
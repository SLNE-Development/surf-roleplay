package dev.slne.surf.roleplay.paper.mechanics.rentable.listeners

import com.github.shynixn.mccoroutine.folia.entityDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.paper.rentable.events.RentableRentCollectEvent
import dev.slne.surf.roleplay.paper.plugin
import dev.slne.surf.surfapi.core.api.messages.adventure.playSound
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object RentCollectedListener : Listener {

    @EventHandler
    fun onRentableRentCollect(event: RentableRentCollectEvent) {
        val rentable = event.rentable
        val player = rentable.owner?.cloudPlayer?.player ?: return

        plugin.launch(plugin.entityDispatcher(player)) {
            player.sendText {
                appendPrefix()

                info("Du hast die Miete in Höhe von ")
                variableValue("${event.amount} €")
                info(" für deine Immobilie ")
                append(rentable)
                info(" bezahlt.")
            }

            player.playSound(true) {
                type(Sound.BLOCK_ANVIL_LAND)
                volume(.5f)
                source(net.kyori.adventure.sound.Sound.Source.BLOCK)
            }
        }
    }

}
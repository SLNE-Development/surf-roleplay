package dev.slne.surf.roleplay.paper.mechanics.rentable.listeners

import dev.slne.surf.cloud.api.common.event.CloudEventHandler
import dev.slne.surf.roleplay.paper.mechanics.rentable.events.RentableRentCollectEvent
import dev.slne.surf.surfapi.core.api.messages.adventure.playSound
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import net.kyori.adventure.sound.Sound.Source
import org.bukkit.Sound
import org.springframework.stereotype.Component

@Suppress("unused")
@Component
class RentCollectedListener {

    @CloudEventHandler
    fun onRentableRentCollect(event: RentableRentCollectEvent) {
        val rentable = event.rentable
        val player = rentable.owner?.cloudPlayer?.player ?: return

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
            source(Source.BLOCK)
        }
    }
}
package dev.slne.surf.roleplay.mechanic.mechanics.idcard.listeners

import com.github.shynixn.mccoroutine.folia.entityDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.mechanic.mechanics.idcard.dialogs.createIdDialog
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.core.api.messages.adventure.playSound
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import org.bukkit.Sound
import org.bukkit.entity.Pig
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent

object IdCardPlayerInteractWithEntityHandler : Listener {

    @EventHandler()
    fun onPlayerInteractAtEntity(event: PlayerInteractAtEntityEvent) {
        val player = event.player
        val entity = event.rightClicked

        if (entity !is Pig) {
            return
        }

        plugin.launch(plugin.entityDispatcher(player)) {
            val rpPlayer = RpPlayer[player.uniqueId]

            if (rpPlayer.isCitizen()) {
                player.sendText {
                    appendPrefix()
                    error("Du hast bereits einen Personalausweis beantragt.")
                }

                player.playSound(true) {
                    type(Sound.BLOCK_NOTE_BLOCK_BASS)
                    volume(.5f)
                    source(net.kyori.adventure.sound.Sound.Source.MUSIC)
                }
                return@launch
            }

            player.showDialog(createIdDialog())
        }
    }
}
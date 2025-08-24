package dev.slne.surf.roleplay.paper.mechanics.idcard.listeners

import com.github.shynixn.mccoroutine.folia.entityDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.npc.api.event.NpcInteractEvent
import dev.slne.surf.npc.api.surfNpcApi
import dev.slne.surf.roleplay.paper.mechanics.idcard.IdCardNpc
import dev.slne.surf.roleplay.paper.mechanics.idcard.dialogs.createIdDialog
import dev.slne.surf.roleplay.paper.player.rpPlayer
import dev.slne.surf.roleplay.paper.plugin
import dev.slne.surf.surfapi.core.api.messages.adventure.playSound
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import net.kyori.adventure.sound.Sound.Source
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.springframework.stereotype.Component

@Component
class IdCardHandler : Listener {

    private val idCardNpc
        get() = surfNpcApi.getNpc(IdCardNpc.NPC_NAME) ?: error("The IDCard NPC is not registered")

    @EventHandler
    fun onNpcInteract(event: NpcInteractEvent) {
        val player = event.player
        val npc = event.npc

        if (npc.npcUuid != idCardNpc.npcUuid) return

        plugin.launch(plugin.entityDispatcher(player)) {
            val rpPlayer = player.rpPlayer

            if (rpPlayer.hasCompletedCitizenship()) {
                player.sendText {
                    appendPrefix()
                    error("Du hast bereits einen Personalausweis beantragt.")
                }

                player.playSound(true) {
                    type(Sound.BLOCK_NOTE_BLOCK_BASS)
                    volume(0.5f)
                    source(Source.MUSIC)
                }
                return@launch
            }

            player.showDialog(createIdDialog())
        }
    }
}
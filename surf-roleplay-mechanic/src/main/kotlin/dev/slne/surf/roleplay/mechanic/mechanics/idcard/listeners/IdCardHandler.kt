package dev.slne.surf.roleplay.mechanic.mechanics.idcard.listeners

import com.github.shynixn.mccoroutine.folia.entityDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.npc.api.event.NpcInteractEvent
import dev.slne.surf.npc.api.surfNpcApi
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.mechanic.mechanics.idcard.IdCardMechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanics.idcard.dialogs.createIdDialog
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.core.api.messages.adventure.playSound
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object IdCardHandler : Listener {

    private val idCardNpc
        get() = surfNpcApi.getNpc(IdCardMechanicImpl.NPC_NAME)
            ?: error("The IDCard NPC is not registered")

    @EventHandler
    fun onNpcInteract(event: NpcInteractEvent) {
        val player = event.player
        val npc = event.npc

        if (npc.npcUuid != idCardNpc.npcUuid) return

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
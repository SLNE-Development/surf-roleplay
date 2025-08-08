package dev.slne.surf.roleplay.mechanic.mechanics.atm.listeners

import com.github.shynixn.mccoroutine.folia.entityDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.npc.api.event.NpcInteractEvent
import dev.slne.surf.npc.api.surfNpcApi
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.mechanic.mechanics.atm.AtmMechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.npc.createNpcMainMenuDialog
import dev.slne.surf.roleplay.mechanic.plugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object NpcInteractHandler : Listener {

    private val idCardNpc
        get() = surfNpcApi.getNpc(AtmMechanicImpl.NPC_NAME)
            ?: error("The Bank NPC is not registered")


    @EventHandler
    fun onNpcInteract(event: NpcInteractEvent) {
        val player = event.player
        val npc = event.npc

        if (npc.npcUuid != idCardNpc.npcUuid) return

        plugin.launch(plugin.entityDispatcher(player)) {
            val rpPlayer = RpPlayer[player.uniqueId]


            player.showDialog(createNpcMainMenuDialog(rpPlayer))

            //open dialog
            //3 buttons: Konto erstellen (only once, otherwise error screen), Pin ändern, Konto löschen, Pin zurücksetzen (- 50%  aus bank abziehen für Pin verraten)

            //ATM: ony accessible with a bank card, which is given from this npc
            //ATM: if player interacts without card -> chat message "card needed" (or open error dialog after interacting)
            //ATM: only owner of card can interact with ATM -> other players will be returned with a chat message / dialog /// maybe also other players should have acess with the card like in real life, they have to bruteforce the pin or something.
            //ATM: Pin reset only available with card
            //ATM: Show owners first and last name in welcoming screen dialog
            //ATM: interactions with atm only possible if player entered pin correctly


            //taxes: 10% of amount -> editable static const val in AtmMechanicImpl.kt
        }
    }
}
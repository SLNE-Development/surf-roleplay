package dev.slne.surf.roleplay.mechanic.mechanics.atm.listeners

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.createAtmMainMenuDialog
import dev.slne.surf.roleplay.mechanic.plugin
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

object AtmHandler : Listener {

    private val bankCardKey = NamespacedKey("surf-roleplay-mechanic", "bank_card")

    @EventHandler()
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player

        val clickedBlock = event.clickedBlock ?: return

        if (clickedBlock.type != Material.NETHERITE_BLOCK) return

        plugin.launch {
            val rpPlayer = RpPlayer[player.uniqueId]
            val item = player.inventory.itemInMainHand

            player.showDialog(createAtmMainMenuDialog(rpPlayer, item.persistentDataContainer.has(bankCardKey)))
        }
        event.isCancelled = true
    }

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
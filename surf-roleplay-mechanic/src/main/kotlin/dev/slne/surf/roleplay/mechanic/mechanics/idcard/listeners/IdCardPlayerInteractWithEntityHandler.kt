package dev.slne.surf.roleplay.mechanic.mechanics.idcard.listeners

import dev.slne.surf.roleplay.mechanic.mechanics.idcard.dialogs.createIdDialog
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
        player.showDialog(createIdDialog())
    }


}
package dev.slne.surf.roleplay.mechanic.mechanics.atm.listeners

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.createAtmMainMenuDialog
import dev.slne.surf.roleplay.mechanic.plugin
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

object AtmHandler : Listener {

    @EventHandler()
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player

        if (!player.isSneaking) return

        val clickedBlock = event.clickedBlock ?: return

        if (clickedBlock.type != Material.NETHERITE_BLOCK) return

        plugin.launch {
            val rpPlayer = RpPlayer[player.uniqueId]
            player.showDialog(createAtmMainMenuDialog(rpPlayer))
        }
    }
}
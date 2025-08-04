package dev.slne.surf.roleplay.mechanic.mechanics.rentable.listeners

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.mechanic.Mechanic
import dev.slne.surf.roleplay.api.mechanic.rentable.RentableMechanic
import dev.slne.surf.roleplay.api.mechanic.rentable.events.RentableOwnerChangeEvent
import dev.slne.surf.roleplay.api.player.events.RpPlayerQuitEvent
import dev.slne.surf.roleplay.mechanic.plugin
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

object RentableOnlineListener : Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun onRpPlayerQuit(event: RpPlayerQuitEvent) {
        val mechanic = Mechanic.getMechanic<RentableMechanic>()
        val ownedRentables = mechanic.getOwnedRentablesByPlayer(event.rpPlayer)

        plugin.launch {
            ownedRentables.forEach { rentable ->
                rentable.setOwner(null, RentableOwnerChangeEvent.OwnerChangeReason.PlayerQuit)
            }
        }
    }

}
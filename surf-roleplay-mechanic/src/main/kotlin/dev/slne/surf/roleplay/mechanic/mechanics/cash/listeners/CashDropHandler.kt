package dev.slne.surf.roleplay.mechanic.mechanics.cash.listeners

import dev.slne.surf.roleplay.api.player.events.RpPlayerDeathEvent
import dev.slne.surf.roleplay.mechanic.mechanics.cash.Cash
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object CashDropHandler : Listener {

    @EventHandler
    fun onDeath(event: RpPlayerDeathEvent) {
        Cash.dropAndRemoveCash(event.rpPlayer)
    }
}

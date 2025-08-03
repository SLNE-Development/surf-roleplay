package dev.slne.surf.roleplay.api.player.events

import dev.slne.surf.roleplay.api.events.RpEvent
import dev.slne.surf.roleplay.api.player.RpPlayer
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByBlockEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent

class RpPlayerDeathEvent(
    val rpPlayer: RpPlayer,
    bukkitEvent: PlayerDeathEvent
) : RpEvent() {

    var killerEntity: Entity?
        private set

    var killerBlock: Block? = null
        private set

    suspend fun killerRpPlayer() = (killerEntity as? Player)?.let { RpPlayer[it.uniqueId] }

    init {
        val player = bukkitEvent.player
        val lastDamage =
            player.lastDamageCause ?: error("Player ${player.name} has no last damage cause")

        killerEntity = player.killer

        if (killerEntity == null) {
            if (lastDamage is EntityDamageByEntityEvent) {
                killerEntity = lastDamage.damager
            } else if (lastDamage is EntityDamageByBlockEvent) {
                killerBlock = lastDamage.damager
            }
        }
    }
}
package dev.slne.surf.roleplay.api.paper.player.events

import dev.slne.surf.cloud.api.client.paper.player.toCloudOfflinePlayer
import dev.slne.surf.roleplay.api.common.player.RpPlayer
import dev.slne.surf.roleplay.api.paper.events.RpEvent
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByBlockEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent

/**
 * Event triggered when a player dies in the game.
 * This event provides information about the player who died, the entity or block that caused the death,
 * and allows access to the killer's RpPlayer instance if applicable.
 *
 * @property player The RpPlayer instance representing the player who died.
 * @property bukkitEvent The original Bukkit PlayerDeathEvent that this event wraps.
 */
class RpPlayerDeathEvent(
    val player: RpPlayer,
    bukkitEvent: PlayerDeathEvent
) : RpEvent() {

    var killerEntity: Entity?
        private set

    var killerBlock: Block? = null
        private set

    fun killerRpPlayer() = (killerEntity as? Player)?.let { RpPlayer[it.toCloudOfflinePlayer()] }

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
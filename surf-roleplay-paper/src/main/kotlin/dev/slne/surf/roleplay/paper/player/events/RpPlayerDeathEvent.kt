package dev.slne.surf.roleplay.paper.player.events

import dev.slne.surf.cloud.api.common.event.CloudEvent
import dev.slne.surf.roleplay.paper.player.PaperRpPlayer
import dev.slne.surf.roleplay.paper.player.rpPlayer
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
    source: Any,
    val player: PaperRpPlayer,
    bukkitEvent: PlayerDeathEvent
) : CloudEvent(source) {

    var killerEntity: Entity?
        private set

    var killerBlock: Block? = null
        private set

    fun killerRpPlayer() = (killerEntity as? Player)?.rpPlayer

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
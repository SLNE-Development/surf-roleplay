package dev.slne.surf.roleplay.api.paper.player.events

import dev.slne.surf.cloud.api.client.paper.player.toCloudOfflinePlayer
import dev.slne.surf.cloud.api.common.event.CloudEvent
import dev.slne.surf.roleplay.api.common.player.RpPlayer
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByBlockEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent

class RpPlayerDeathEvent(
    source: Any,
    val player: RpPlayer,
    bukkitEvent: PlayerDeathEvent
) : CloudEvent(source) {

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
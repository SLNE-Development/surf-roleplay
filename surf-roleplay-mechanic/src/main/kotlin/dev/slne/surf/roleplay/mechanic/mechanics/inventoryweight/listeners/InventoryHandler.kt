package dev.slne.surf.roleplay.mechanic.mechanics.inventoryweight.listeners

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import com.github.shynixn.mccoroutine.folia.launch
import com.github.shynixn.mccoroutine.folia.ticks
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.rpPlayer
import dev.slne.surf.roleplay.mechanic.mechanics.inventoryweight.ItemWeightRule
import dev.slne.surf.roleplay.mechanic.mechanics.inventoryweight.RpPlayerChangeWeightEvent
import dev.slne.surf.roleplay.mechanic.mechanics.inventoryweight.WeightMap
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import kotlinx.coroutines.delay
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.player.PlayerDropItemEvent

object InventoryHandler : Listener {

    val weightMap by lazy {
        WeightMap.entries.sortedByDescending { it.weight }
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        plugin.launch {
            callEvent((player.rpPlayer()))
        }
    }

    @EventHandler
    fun onInventoryDrag(event: InventoryDragEvent) {
        val player = event.whoClicked as? Player ?: return
        plugin.launch {
            callEvent((player.rpPlayer()))
        }
    }

    @EventHandler
    fun onItemDrop(event: PlayerDropItemEvent) {
        plugin.launch {
            callEvent((event.player.rpPlayer()))
        }
    }

    @EventHandler
    fun onPlayerPickItem(event: EntityPickupItemEvent) {
        val player = event.entity as? Player ?: return

        plugin.launch {
            callEvent((player.rpPlayer()))
        }
    }

    private suspend fun callEvent(player: RpPlayer) {
        val bukkitPlayer = player.bukkitPlayer ?: return
        val currentWeight = calculateInventoryWeight(bukkitPlayer)
        delay(1.ticks)
        val newWeight = calculateInventoryWeight(bukkitPlayer)

        if (currentWeight != newWeight) { // FIXME: This check is commented out, but it might be needed to prevent unnecessary events
            RpPlayerChangeWeightEvent(player, currentWeight, newWeight).callEvent()
        }
    }

    @EventHandler
    fun onRpPlayerChangeWeight(event: RpPlayerChangeWeightEvent) {
        plugin.launch {
            event.player.bukkitPlayer?.let { setPlayerMovementSpeed(it, event.to) }
        }
    }

    @EventHandler
    fun onPlayerJump(event: PlayerJumpEvent) {
        val weight = calculateInventoryWeight(event.player)
        val cancelsJump = getWeightMapEntry(weight).cancelsJump

        if (cancelsJump) {
            event.player.sendActionBar(buildText {
                error("Du bist zu schwer, um zu springen!")
            })
        }
        event.isCancelled = cancelsJump

    }

    private fun getWeightMapEntry(weight: Double) = weightMap.firstOrNull { weight >= it.weight } ?: WeightMap.NORMAL

    private fun setPlayerMovementSpeed(player: Player, weight: Double) {
        val attribute = player.getAttribute(Attribute.MOVEMENT_SPEED) ?: return
        val entry = getWeightMapEntry(weight)

        attribute.baseValue = entry.speed
    }

    private fun calculateInventoryWeight(player: Player): Double {
        val total = player.inventory.contents
            .filterNotNull()
            .sumOf { item -> ItemWeightRule.getWeight(item) * item.amount }
        return total
    }
}

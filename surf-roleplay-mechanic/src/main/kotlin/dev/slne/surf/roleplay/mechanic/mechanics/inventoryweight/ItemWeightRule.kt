package dev.slne.surf.roleplay.mechanic.mechanics.inventoryweight

import dev.slne.surf.roleplay.mechanic.mechanics.idcard.IdCard
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class ItemWeightRule(
    val material: Material,
    val weight: Double,
    val matches: (ItemStack) -> Boolean = { true }
) {
    BEDROCK_BLOCK(Material.BEDROCK, 1000.0),
    ID_CARD(
        Material.FEATHER, 200.0,
        matches = { item ->
            val pdc = item.persistentDataContainer
            pdc.has(IdCard.idCardKey)
        }
    );

    companion object {
        fun getWeight(item: ItemStack): Double {
            val rule = entries.firstOrNull { it.material == item.type && it.matches(item) }
            return rule?.weight ?: 0.0
        }
    }
}
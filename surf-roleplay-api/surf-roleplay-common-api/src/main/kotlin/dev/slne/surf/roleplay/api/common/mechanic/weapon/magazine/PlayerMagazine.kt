package dev.slne.surf.roleplay.api.mechanic.weapon.magazine

import org.bukkit.inventory.ItemStack

interface PlayerMagazine {

    val magazine: Magazine

    val currentAmmo: Int

    fun buildItemStack(): ItemStack

}
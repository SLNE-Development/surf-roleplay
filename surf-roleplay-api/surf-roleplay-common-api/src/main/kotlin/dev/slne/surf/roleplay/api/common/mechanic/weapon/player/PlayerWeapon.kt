package dev.slne.surf.roleplay.api.mechanic.weapon.player

import dev.slne.surf.roleplay.api.mechanic.weapon.Weapon
import dev.slne.surf.roleplay.api.mechanic.weapon.magazine.PlayerMagazine
import org.bukkit.inventory.ItemStack

interface PlayerWeapon {

    val weapon: Weapon

    val currentMagazine: PlayerMagazine?

    fun setCurrentMagazine(magazine: PlayerMagazine?)
    
    fun buildItemStack(): ItemStack
}
package dev.slne.surf.roleplay.api.common.mechanic.weapon.player

import dev.slne.surf.roleplay.api.common.mechanic.weapon.Weapon
import dev.slne.surf.roleplay.api.common.mechanic.weapon.magazine.PlayerMagazine
import org.bukkit.inventory.ItemStack

interface PlayerWeapon {

    val weapon: Weapon

    val currentMagazine: PlayerMagazine?

    fun setCurrentMagazine(magazine: PlayerMagazine?)

    fun buildItemStack(): ItemStack
}
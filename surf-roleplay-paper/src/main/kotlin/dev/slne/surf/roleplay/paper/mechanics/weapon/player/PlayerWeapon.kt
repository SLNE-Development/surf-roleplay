package dev.slne.surf.roleplay.paper.mechanics.weapon.player

import dev.slne.surf.roleplay.paper.mechanics.weapon.Weapon
import dev.slne.surf.roleplay.paper.mechanics.weapon.magazine.PlayerMagazine

class PlayerWeapon(val weapon: Weapon) {
    var currentMagazine: PlayerMagazine? = null
        private set

    fun setCurrentMagazine(magazine: PlayerMagazine?) {
        currentMagazine = magazine
    }
}
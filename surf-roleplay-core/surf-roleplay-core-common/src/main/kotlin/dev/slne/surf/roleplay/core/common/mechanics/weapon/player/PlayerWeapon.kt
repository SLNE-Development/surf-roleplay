package dev.slne.surf.roleplay.core.common.mechanics.weapon.player

import dev.slne.surf.roleplay.core.common.mechanics.weapon.Weapon
import dev.slne.surf.roleplay.core.common.mechanics.weapon.magazine.PlayerMagazine

class PlayerWeapon(
    val weapon: Weapon
) {
    private var _currentMagazine: PlayerMagazine? = null
    val currentMagazine get() = _currentMagazine

    fun setCurrentMagazine(magazine: PlayerMagazine?) {
        _currentMagazine = magazine
    }
}
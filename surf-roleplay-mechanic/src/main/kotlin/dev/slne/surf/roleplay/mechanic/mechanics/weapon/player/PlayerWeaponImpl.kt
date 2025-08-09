package dev.slne.surf.roleplay.mechanic.mechanics.weapon.player

import dev.slne.surf.roleplay.api.mechanic.weapon.Weapon
import dev.slne.surf.roleplay.api.mechanic.weapon.magazine.PlayerMagazine
import dev.slne.surf.roleplay.api.mechanic.weapon.player.PlayerWeapon

class PlayerWeaponImpl(
    override val weapon: Weapon
) : PlayerWeapon {
    private var _currentMagazine: PlayerMagazine? = null
    override val currentMagazine get() = _currentMagazine

    override fun setCurrentMagazine(magazine: PlayerMagazine?) {
        _currentMagazine = magazine
    }
}
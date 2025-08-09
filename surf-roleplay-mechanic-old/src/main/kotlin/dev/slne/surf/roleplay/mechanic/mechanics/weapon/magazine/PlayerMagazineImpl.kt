package dev.slne.surf.roleplay.mechanic.mechanics.weapon.magazine

import dev.slne.surf.roleplay.api.mechanic.weapon.magazine.Magazine
import dev.slne.surf.roleplay.api.mechanic.weapon.magazine.PlayerMagazine

class PlayerMagazineImpl(
    override val magazine: Magazine,
) : PlayerMagazine {
    private var _currentAmmo = magazine.maxAmmo
    override val currentAmmo get() = _currentAmmo
}
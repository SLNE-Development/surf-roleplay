package dev.slne.surf.roleplay.core.common.mechanics.weapon.magazine

class PlayerMagazine(
    val magazine: Magazine,
) {
    private var _currentAmmo = magazine.maxAmmo
    val currentAmmo get() = _currentAmmo
}
package dev.slne.surf.roleplay.paper.mechanics.weapon.magazine

class PlayerMagazine(val magazine: Magazine) {
    var currentAmmo = magazine.maxAmmo
        private set
}
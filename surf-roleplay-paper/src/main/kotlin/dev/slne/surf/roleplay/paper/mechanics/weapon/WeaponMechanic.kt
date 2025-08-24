package dev.slne.surf.roleplay.paper.mechanics.weapon

import dev.slne.surf.roleplay.paper.mechanics.AbstractMechanic
import dev.slne.surf.roleplay.paper.mechanics.Mechanic
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf

@Mechanic("WeaponMechanic")
class WeaponMechanic : AbstractMechanic() {
    private val _weapons = mutableObjectSetOf<Weapon>()
    val weapons = _weapons.freeze()
}
package dev.slne.surf.roleplay.paper.mechanics.weapon

import dev.slne.surf.roleplay.paper.mechanics.AbstractMechanic
import dev.slne.surf.roleplay.paper.mechanics.Mechanic
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf

@Mechanic
class WeaponMechanic : AbstractMechanic("WeaponMechanic") {
    private val _weapons = mutableObjectSetOf<Weapon>()
    val weapons = _weapons.freeze()
}
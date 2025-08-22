package dev.slne.surf.roleplay.core.common.mechanics.weapon

import dev.slne.surf.roleplay.core.common.mechanics.Mechanic
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf

object WeaponMechanic : Mechanic(
    name = "WeaponMechanic",
) {
    private val _weapons = mutableObjectSetOf<Weapon>()
    val weapons get() = _weapons.freeze()
}
package dev.slne.surf.roleplay.mechanic.mechanics.weapon

import dev.slne.surf.roleplay.api.mechanic.weapon.Weapon
import dev.slne.surf.roleplay.api.mechanic.weapon.WeaponMechanic
import dev.slne.surf.roleplay.mechanic.MechanicImpl
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object WeaponMechanicImpl : MechanicImpl(
    name = "WeaponMechanic",
    handlers = objectSetOf(),
    rpJobs = objectSetOf(),
    packetListeners = objectSetOf()
), WeaponMechanic {
    private val _weapons = mutableObjectSetOf<Weapon>()
    override val weapons get() = _weapons.freeze()
}
package dev.slne.surf.roleplay.api.mechanic.weapon

import dev.slne.surf.roleplay.api.common.mechanic.weapon.Weapon
import it.unimi.dsi.fastutil.objects.ObjectSet
import org.jetbrains.annotations.Unmodifiable

interface WeaponMechanic {

    val weapons: @Unmodifiable ObjectSet<Weapon>

}
package dev.slne.surf.roleplay.api.mechanic.weapon

import it.unimi.dsi.fastutil.objects.ObjectSet
import org.jetbrains.annotations.Unmodifiable

interface WeaponMechanic {

    val weapons: @Unmodifiable ObjectSet<Weapon>

}
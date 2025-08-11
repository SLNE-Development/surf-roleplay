package dev.slne.surf.roleplay.api.common.mechanic.weapon

import dev.slne.surf.roleplay.api.common.mechanic.Mechanic
import it.unimi.dsi.fastutil.objects.ObjectSet
import org.jetbrains.annotations.Unmodifiable

interface WeaponMechanic : Mechanic {

    val weapons: @Unmodifiable ObjectSet<Weapon>

}
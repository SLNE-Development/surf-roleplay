package dev.slne.surf.roleplay.core.common.mechanics.weapon

import dev.slne.surf.roleplay.core.common.mechanics.weapon.magazine.Magazine
import dev.slne.surf.roleplay.core.common.mechanics.weapon.utils.WeaponType
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.key.Key
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

abstract class Weapon(
    val type: WeaponType,
    val weaponMaterial: Key,
    val name: String,
    val displayName: SurfComponentBuilder.() -> Unit = {
        primary(name)
    },
    val description: SurfComponentBuilder.() -> Unit = {
        spacer("Eine ")
        append(displayName)
        spacer(" Waffe")
    },
    val weaponShootCooldown: Duration = 1.seconds,
    val supportedMagazines: ObjectSet<Magazine> = objectSetOf()
) {
    fun isApplicableMagazine(magazine: Magazine) = magazine in supportedMagazines
}
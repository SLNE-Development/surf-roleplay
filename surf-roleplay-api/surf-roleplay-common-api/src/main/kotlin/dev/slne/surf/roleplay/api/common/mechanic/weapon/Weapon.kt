package dev.slne.surf.roleplay.api.common.mechanic.weapon

import dev.slne.surf.roleplay.api.common.mechanic.weapon.magazine.Magazine
import dev.slne.surf.roleplay.api.common.mechanic.weapon.utils.WeaponType
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import kotlin.time.Duration

interface Weapon {

    val type: WeaponType

    val name: String

    val weaponMaterial: Key

    val displayName: SurfComponentBuilder.() -> Unit

    val description: Component

    val weaponShootCooldown: Duration

    val supportedMagazines: ObjectSet<Magazine>

    fun isApplicableMagazine(magazine: Magazine): Boolean

}
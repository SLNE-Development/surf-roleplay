package dev.slne.surf.roleplay.api.mechanic.weapon

import dev.slne.surf.roleplay.api.mechanic.weapon.magazine.Magazine
import dev.slne.surf.roleplay.api.mechanic.weapon.utils.WeaponType
import dev.slne.surf.surfapi.bukkit.api.builder.LoreBuilder
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import it.unimi.dsi.fastutil.objects.ObjectSet
import org.bukkit.Material
import kotlin.time.Duration

interface Weapon {

    val type: WeaponType

    val name: String

    val weaponMaterial: Material

    val displayName: SurfComponentBuilder.() -> Unit

    val description: LoreBuilder.() -> Unit

    val weaponShootCooldown: Duration

    val supportedMagazines: ObjectSet<Magazine>

    fun isApplicableMagazine(magazine: Magazine): Boolean

}
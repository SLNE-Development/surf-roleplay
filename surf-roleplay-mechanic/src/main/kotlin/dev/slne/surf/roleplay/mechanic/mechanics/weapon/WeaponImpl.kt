package dev.slne.surf.roleplay.mechanic.mechanics.weapon

import dev.slne.surf.roleplay.api.mechanic.weapon.Weapon
import dev.slne.surf.roleplay.api.mechanic.weapon.magazine.Magazine
import dev.slne.surf.roleplay.api.mechanic.weapon.utils.WeaponType
import dev.slne.surf.surfapi.bukkit.api.builder.LoreBuilder
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import it.unimi.dsi.fastutil.objects.ObjectSet
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

abstract class WeaponImpl(
    override val type: WeaponType,
    override val weaponMaterial: Material,
    override val name: String,
    override val displayName: SurfComponentBuilder.() -> Unit = {
        primary(name)
    },
    override val description: LoreBuilder.() -> Unit = {
        line {
            spacer("Eine ")
            append(displayName)
            spacer(" Waffe")
        }
    },
    override val weaponShootCooldown: Duration = 1.seconds,
    override val supportedMagazines: ObjectSet<Magazine> = objectSetOf()
) : Weapon {
    override fun isApplicableMagazine(magazine: Magazine) = magazine in supportedMagazines

    override fun buildItemStack(): ItemStack {

    }
}
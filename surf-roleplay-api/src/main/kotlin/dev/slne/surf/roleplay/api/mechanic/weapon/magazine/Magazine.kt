package dev.slne.surf.roleplay.api.mechanic.weapon.magazine

import dev.slne.surf.surfapi.bukkit.api.builder.LoreBuilder
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder

interface Magazine {

    val name: String

    val displayName: SurfComponentBuilder.() -> Unit

    val description: LoreBuilder.() -> Unit

    val maxAmmo: Int

    /**
     * Function to calculate the damage dealt by the weapon using this magazine.
     * The function takes the distance to the target hit as a parameter and returns the damage dealt.
     */
    val damageFunction: (Double) -> Double

}
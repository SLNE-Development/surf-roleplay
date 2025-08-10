package dev.slne.surf.roleplay.api.common.mechanic.weapon.magazine

import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import net.kyori.adventure.text.Component

interface Magazine {

    /**
     * The unique identifier of the magazine.
     * This is used to identify the magazine in the system.
     */
    val name: String

    /**
     * The display name of the magazine.
     * This is a function that allows you to build the display name using a [SurfComponentBuilder].
     */
    val displayName: SurfComponentBuilder.() -> Unit

    /**
     * The description of the magazine.
     */
    val description: Component

    /**
     * The maximum amount of ammo that can be loaded into this magazine.
     * This is used to determine how many bullets the magazine can hold.
     */
    val maxAmmo: Int

    /**
     * Function to calculate the damage dealt by the weapon using this magazine.
     * The function takes the distance to the target hit as a parameter and returns the damage dealt.
     */
    val damageFunction: (Double) -> Double

}
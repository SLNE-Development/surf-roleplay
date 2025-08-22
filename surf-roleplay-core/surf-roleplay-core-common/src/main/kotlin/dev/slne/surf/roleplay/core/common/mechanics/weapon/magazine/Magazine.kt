package dev.slne.surf.roleplay.core.common.mechanics.weapon.magazine

import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder

abstract class Magazine(
    val name: String,
    val displayName: SurfComponentBuilder.() -> Unit = {
        primary(name)
    },
    val description: SurfComponentBuilder.() -> Unit = {
        spacer("Ein ")
        append(displayName)
        spacer(" Magazin")
    },
    val maxAmmo: Int,
    val damageFunction: (Double) -> Double
)
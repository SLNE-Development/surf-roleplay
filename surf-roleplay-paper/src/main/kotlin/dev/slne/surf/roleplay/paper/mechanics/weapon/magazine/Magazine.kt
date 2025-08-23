package dev.slne.surf.roleplay.paper.mechanics.weapon.magazine

import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder

abstract class Magazine(
    val name: String,
    displayName: SurfComponentBuilder.() -> Unit = {
        primary(name)
    },
    description: SurfComponentBuilder.() -> Unit = {
        spacer("Ein ")
        append(displayName)
        spacer(" Magazin")
    },
    val maxAmmo: Int,
    val damageFunction: (Double) -> Double
) {
    val displayName = buildText(displayName)
    val description = buildText(description)
}
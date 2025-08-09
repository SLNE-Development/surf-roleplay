package dev.slne.surf.roleplay.mechanic.mechanics.weapon.magazine

import dev.slne.surf.roleplay.api.mechanic.weapon.magazine.Magazine
import dev.slne.surf.surfapi.bukkit.api.builder.LoreBuilder
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder

abstract class MagazineImpl(
    override val name: String,
    override val displayName: SurfComponentBuilder.() -> Unit = {
        primary(name)
    },
    override val description: LoreBuilder.() -> Unit = {
        line {
            spacer("Ein ")
            append(displayName)
            spacer(" Magazin")
        }
    },
    override val maxAmmo: Int,
    override val damageFunction: (Double) -> Double
) : Magazine
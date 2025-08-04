package dev.slne.surf.roleplay.mechanic.mechanics.map

import dev.slne.surf.roleplay.api.mechanic.map.Map
import dev.slne.surf.roleplay.api.mechanic.rentable.Rentable
import dev.slne.surf.surfapi.bukkit.api.builder.LoreBuilder
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.key.Key

abstract class MapImpl(
    override val key: Key,
    override val name: String,
    override val displayName: SurfComponentBuilder.() -> Unit,
    override val description: LoreBuilder.() -> Unit,
    override val rentables: ObjectSet<Rentable> = objectSetOf()
) : Map {
    override fun asComponent() = SurfComponentBuilder.builder().apply(displayName).build()
}
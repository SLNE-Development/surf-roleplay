package dev.slne.surf.roleplay.api.mechanic.map

import dev.slne.surf.roleplay.api.mechanic.rentable.Rentable
import dev.slne.surf.surfapi.bukkit.api.builder.LoreBuilder
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.ComponentLike

interface Map : ComponentLike {

    /**
     * The key that uniquely identifies this map.
     */
    val key: Key

    /**
     * The name of the map.
     */
    val name: String

    /**
     * The lore of the map, which is a description that can be displayed in the UI.
     * This is built using a [LoreBuilder].
     */
    val description: LoreBuilder.() -> Unit

    /**
     * The display name of the map, which is used in the UI.
     */
    val displayName: SurfComponentBuilder.() -> Unit

    /**
     * A set of rentable properties associated with this map.
     */
    val rentables: ObjectSet<Rentable>

}
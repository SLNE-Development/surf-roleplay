package dev.slne.surf.roleplay.api.mechanic.license

import dev.slne.surf.roleplay.api.mechanic.license.utils.UnobtainableReason
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.surfapi.bukkit.api.builder.LoreBuilder
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import kotlin.time.Duration

interface License {
    /**
     * The internal name of the license, used for identification.
     */
    val key: Key

    /**
     * The display name of the license, shown to players.
     */
    val displayName: Component

    /**
     * A function that builds the description of the license using a [LoreBuilder].
     * This allows for a flexible and formatted description.
     */
    val description: LoreBuilder.() -> Unit

    /**
     * The cost of the license in the game currency.
     * This is the amount a player must pay to obtain the license.
     */
    val cost: Double

    /**
     * The duration for which the license is valid.
     * If null, the license does not expire.
     */
    val expiresIn: Duration?

    /**
     * A set of other licenses that must be obtained before this one.
     * This allows for dependencies between licenses.
     */
    val dependencies: ObjectSet<License>

    /**
     * The permission required to obtain this license.
     * This is used to check if a player has the necessary permissions.
     */
    val permission: String

    /**
     * Checks if the player can obtain this license.
     * Returns a pair where the first element is a boolean indicating success,
     * and the second element is an [UnobtainableReason] if applicable.
     *
     * @param player The player who is trying to obtain the license.
     * @return A pair containing a boolean indicating if the player can obtain the license,
     */
    suspend fun canObtain(player: RpPlayer): Pair<Boolean, UnobtainableReason?>
}
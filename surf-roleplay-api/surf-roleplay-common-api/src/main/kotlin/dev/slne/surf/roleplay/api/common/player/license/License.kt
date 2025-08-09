package dev.slne.surf.roleplay.api.common.player.license

import dev.slne.surf.roleplay.api.common.player.identity.RpIdentity
import dev.slne.surf.roleplay.api.common.player.license.utils.UnobtainableReason
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component

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
     * A description of the license, providing more details about its purpose and benefits.
     */
    val description: Component

    /**
     * The price of the license in the game currency.
     * This is the amount a player must pay to obtain the license.
     */
    val price: Int

    /**
     * A set of other licenses that must be obtained before this one.
     * This allows for dependencies between licenses.
     */
    val dependencies: ObjectSet<License>

    /**
     * A set of child licenses that can be obtained after this one.
     * This allows for a hierarchy of licenses.
     */
    val children: ObjectSet<License>

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
     * @param identity The identity of the player that is trying to obtain the license.
     * @return A pair containing a boolean indicating if the player can obtain the license,
     */
    suspend fun canObtain(identity: RpIdentity): Pair<Boolean, ObjectSet<UnobtainableReason>>
}
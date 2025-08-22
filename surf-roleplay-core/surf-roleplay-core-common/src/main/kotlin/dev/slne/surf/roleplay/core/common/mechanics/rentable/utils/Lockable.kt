package dev.slne.surf.roleplay.core.common.mechanics.rentable.utils

import dev.slne.surf.roleplay.core.common.player.RpPlayer

/**
 * Represents an entity that can be locked and accessed by players.
 */
interface Lockable {
    /**
     * Checks if the player can access this lockable.
     *
     * This also checks if the player has access via being broken into
     *
     * @param player The player attempting to access the lockable.
     * @return `true` if the player can access the lockable, `false` otherwise.
     */
    fun canAccess(player: RpPlayer): Boolean
}
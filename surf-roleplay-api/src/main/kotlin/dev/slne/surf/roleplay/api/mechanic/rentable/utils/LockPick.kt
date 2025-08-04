package dev.slne.surf.roleplay.api.mechanic.rentable.utils

import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder

interface LockPick {

    /**
     * The display name of the lock pick.
     */
    val displayName: SurfComponentBuilder.() -> Unit

    /**
     * The chance of successfully picking the lock.
     * A value between 0.0 (0%) and 1.0 (100%).
     */
    val breakChance: (RpPlayer) -> Double

    enum class LockPickResult {
        /**
         * The lockpick was successful.
         */
        SUCCESS,

        /**
         * The lockpick was broken during the process.
         */
        BROKEN,

        /**
         * The player moved in the middle of the lock picking process.
         */
        PLAYER_MOVED,

        /**
         * The event was cancelled
         */
        EVENT_CANCELLED
    }

}
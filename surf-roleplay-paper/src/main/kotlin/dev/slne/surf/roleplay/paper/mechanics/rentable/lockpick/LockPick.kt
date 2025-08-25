package dev.slne.surf.roleplay.paper.mechanics.rentable.lockpick

import dev.slne.surf.roleplay.core.common.player.RpPlayer
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder

abstract class LockPick(
    displayName: SurfComponentBuilder.() -> Unit,
    val breakChance: (RpPlayer) -> Double
) {
    val displayName = buildText(displayName)

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
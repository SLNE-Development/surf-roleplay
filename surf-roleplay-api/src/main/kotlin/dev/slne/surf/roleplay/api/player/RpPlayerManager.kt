package dev.slne.surf.roleplay.api.player

import dev.slne.surf.surfapi.core.api.util.requiredService
import java.util.*

private val rpPlayerManager get() = requiredService<RpPlayerManager>()

interface RpPlayerManager {

    /**
     * Gets a [RpPlayer] by their UUID.
     *
     * @param uuid the UUID of the player
     * @return the [RpPlayer] associated with the UUID
     */
    suspend operator fun get(uuid: UUID): RpPlayer

    companion object : RpPlayerManager by rpPlayerManager {
        /**
         * The singleton instance of [RpPlayerManager].
         */
        val INSTANCE get() = rpPlayerManager
    }

}
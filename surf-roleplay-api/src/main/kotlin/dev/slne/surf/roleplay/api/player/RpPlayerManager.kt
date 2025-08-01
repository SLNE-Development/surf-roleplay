package dev.slne.surf.roleplay.api.player

import dev.slne.surf.surfapi.core.api.util.requiredService
import it.unimi.dsi.fastutil.objects.ObjectSet
import org.jetbrains.annotations.Unmodifiable
import java.util.*

private val rpPlayerManager get() = requiredService<RpPlayerManager>()

interface RpPlayerManager {

    /**
     * The set of all [RpPlayer]s currently managed by the [RpPlayerManager].
     */
    val players: @Unmodifiable ObjectSet<RpPlayer>

    /**
     * Gets a [RpPlayer] by their UUID.
     *
     * @param uuid the UUID of the player
     * @return the [RpPlayer] associated with the UUID
     */
    operator fun get(uuid: UUID): RpPlayer

    companion object : RpPlayerManager by rpPlayerManager {
        /**
         * The singleton instance of [RpPlayerManager].
         */
        val INSTANCE get() = rpPlayerManager
    }

}
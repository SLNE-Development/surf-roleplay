package dev.slne.surf.roleplay.api.player

import dev.slne.surf.surfapi.core.api.util.requiredService
import it.unimi.dsi.fastutil.objects.ObjectSet
import org.jetbrains.annotations.Unmodifiable
import java.util.*

private val rpPlayerManager = requiredService<RpPlayerManager>()

interface RpPlayerManager {

    /**
     * A set of all [RpPlayer] instances currently managed by this manager.
     */
    val players: @Unmodifiable ObjectSet<RpPlayer>

    /**
     * Gets a [RpPlayer] by their UUID.
     *
     * @param uuid the UUID of the player
     * @return the [RpPlayer] associated with the UUID
     */
    suspend operator fun get(uuid: UUID): RpPlayer

    /**
     * Gets a [RpPlayer] by their username.
     *
     * @param username the username of the player
     * @return the [RpPlayer] associated with the username, or null if not found
     */
    suspend fun getByName(username: String): RpPlayer?

    companion object : RpPlayerManager by rpPlayerManager {
        /**
         * The singleton instance of [RpPlayerManager].
         */
        val INSTANCE get() = rpPlayerManager
    }

}
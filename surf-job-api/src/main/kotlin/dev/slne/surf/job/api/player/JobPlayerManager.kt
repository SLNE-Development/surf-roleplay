package dev.slne.surf.job.api.player

import dev.slne.surf.surfapi.core.api.util.requiredService
import it.unimi.dsi.fastutil.objects.ObjectSet
import org.jetbrains.annotations.Unmodifiable
import java.util.*

private val jobPlayerManager get() = requiredService<JobPlayerManager>()

interface JobPlayerManager {

    /**
     * The set of all [JobPlayer]s currently managed by the [JobPlayerManager].
     */
    val players: @Unmodifiable ObjectSet<JobPlayer>

    /**
     * Gets a [JobPlayer] by their UUID.
     *
     * @param uuid the UUID of the player
     * @return the [JobPlayer] associated with the UUID
     */
    operator fun get(uuid: UUID): JobPlayer

    companion object : JobPlayerManager by jobPlayerManager {
        /**
         * The singleton instance of [JobPlayerManager].
         */
        val INSTANCE get() = jobPlayerManager
    }

}
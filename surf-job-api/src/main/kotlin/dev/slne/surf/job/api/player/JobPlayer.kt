package dev.slne.surf.job.api.player

import dev.slne.surf.job.api.job.Job
import dev.slne.surf.job.api.job.JobRegistry
import dev.slne.surf.job.api.job.getJob
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.*

interface JobPlayer {

    /**
     * The [UUID] of the player
     */
    val uuid: UUID

    /**
     * The current [Job] of the player
     */
    var currentJob: Job

    /**
     * The Bukkit [Player] representation of this player, if they are online.
     * Returns `null` if the player is not online.
     */
    val bukkitPlayer: Player?

    /**
     * The Bukkit [OfflinePlayer] representation of this player.
     */
    val bukkitOfflinePlayer: OfflinePlayer

    /**
     * Changes the player's current [Job].
     *
     * @param job The new [Job] to set.
     * @return `true` if the job was changed successfully, `false` otherwise.
     */
    fun changeJob(job: Job): Boolean

    companion object {
        /**
         * Gets a [JobPlayer] by their [UUID].
         */
        operator fun get(uuid: UUID) = JobPlayerManager[uuid]
    }

}

/**
 * Changes the player's current job to the specified [Job].
 *
 * @return `true` if the job was changed successfully, `false` otherwise.
 */
inline fun <reified T : Job> JobPlayer.changeJob() =
    changeJob(JobRegistry.getJob<T>())
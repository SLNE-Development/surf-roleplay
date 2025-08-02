package dev.slne.surf.job.api.player

import dev.slne.surf.job.api.job.Job
import dev.slne.surf.job.api.job.JobRegistry
import dev.slne.surf.roleplay.api.player.RpPlayer

interface JobPlayer {

    /**
     * The [RpPlayer] associated with this job player.
     */
    val rpPlayer: RpPlayer

    /**
     * The player's current [Job].
     */
    val currentJob: Job

    /**
     * Changes the player's current [Job].
     *
     * @param job The new [Job] to set.
     * @return `true` if the job was changed successfully, `false` otherwise.
     */
    fun changeJob(job: Job): Boolean

    /**
     * Changes the player's current job to the specified type.
     *
     * @param job The class of the job to change to.
     * @return `true` if the job was changed successfully, `false` otherwise.
     */
    fun changeJob(job: Class<out Job>): Boolean = changeJob(JobRegistry.getJob(job))

}

/**
 * Changes the player's current job to the specified type.
 *
 * @param T The type of the job to change to.
 * @return `true` if the job was changed successfully, `false` otherwise.
 */
inline fun <reified T : Job> JobPlayer.changeJob(): Boolean = changeJob(T::class.java)
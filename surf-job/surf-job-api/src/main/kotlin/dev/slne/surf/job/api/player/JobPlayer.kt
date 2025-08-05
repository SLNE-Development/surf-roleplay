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
     * @return The result of the job change operation.
     */
    fun changeJob(job: Job): Job.JobChangeResult

    /**
     * Changes the player's current job to the specified type.
     *
     * @param job The class of the job to change to.
     * @return The result of the job change operation.
     */
    fun changeJob(job: Class<out Job>): Job.JobChangeResult = changeJob(JobRegistry.getJob(job))
}

/**
 * Changes the player's current job to the specified type.
 *
 * @param T The type of the job to change to.
 * @return The result of the job change operation.
 */
inline fun <reified T : Job> JobPlayer.changeJob(): Job.JobChangeResult = changeJob(T::class.java)
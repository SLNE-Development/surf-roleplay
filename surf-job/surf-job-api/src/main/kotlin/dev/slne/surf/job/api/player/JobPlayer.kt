package dev.slne.surf.job.api.player

import dev.slne.surf.job.api.job.Job
import dev.slne.surf.job.api.job.JobRegistry
import dev.slne.surf.job.api.job.getJob
import dev.slne.surf.roleplay.api.player.RpPlayer

/**
 * Represents the players current [Job].
 */
val RpPlayer.currentJob get() = JobRegistry.findJobByPlayer(this)

/**
 * Changes the player's current [Job].
 *
 * @param job The new [Job] to set.
 * @return `true` if the job was changed successfully, `false` otherwise.
 */
fun RpPlayer.changeJob(job: Job): Boolean = job.assignPlayer(this)

/**
 * Changes the player's current job to the specified [Job].
 *
 * @return `true` if the job was changed successfully, `false` otherwise.
 */
inline fun <reified T : Job> RpPlayer.changeJob() =
    changeJob(JobRegistry.getJob<T>())
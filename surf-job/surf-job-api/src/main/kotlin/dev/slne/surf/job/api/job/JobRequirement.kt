package dev.slne.surf.job.api.job

import dev.slne.surf.roleplay.api.player.RpPlayer

fun interface JobRequirement {

    /**
     * Checks if a requirement for the job is met
     *
     * @param job the [Job]
     * @param player the [RpPlayer] to check the requirement for
     * @return if the requirement is met
     */
    fun check(job: Job, player: RpPlayer): Boolean

}
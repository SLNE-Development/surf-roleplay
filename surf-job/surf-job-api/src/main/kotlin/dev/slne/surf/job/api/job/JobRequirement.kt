package dev.slne.surf.job.api.job

import dev.slne.surf.job.api.player.JobPlayer
import dev.slne.surf.surfapi.bukkit.api.builder.LoreBuilder

/**
 * Represents a requirement for a job that must be met for a player to join or keep the job.
 *
 * @property description the description of the requirement, used in lore
 */
abstract class JobRequirement(
    val description: LoreBuilder.() -> Unit
) {

    /**
     * Checks if a requirement for the job is met
     *
     * @param job the [Job]
     * @param player the [JobPlayer] to check the requirement for
     * @return if the requirement is met
     */
    abstract fun check(job: Job, player: JobPlayer): Boolean

}
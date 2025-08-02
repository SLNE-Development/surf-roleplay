package dev.slne.surf.job.api.job.requirements

import dev.slne.surf.job.api.job.Job
import dev.slne.surf.job.api.job.JobRegistry
import dev.slne.surf.job.api.job.JobRequirement
import dev.slne.surf.job.api.job.getJob
import dev.slne.surf.job.api.job.jobs.gang.RebelJob
import dev.slne.surf.job.api.player.JobPlayer

class MinRebelJobRequirement(val menRebel: Int) : JobRequirement {
    override fun check(
        job: Job,
        player: JobPlayer
    ) = JobRegistry.getJob<RebelJob>().players.size >= menRebel
}
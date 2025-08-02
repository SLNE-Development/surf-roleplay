package dev.slne.surf.job.api.job.requirements

import dev.slne.surf.job.api.job.Job
import dev.slne.surf.job.api.job.JobRegistry
import dev.slne.surf.job.api.job.JobRequirement
import dev.slne.surf.job.api.job.getJob
import dev.slne.surf.job.api.job.jobs.state.PoliceJob
import dev.slne.surf.roleplay.api.player.RpPlayer

class MinPoliceJobRequirement(val minPolice: Int) : JobRequirement {
    override fun check(
        job: Job,
        player: RpPlayer
    ) = JobRegistry.getJob<PoliceJob>().players.size >= minPolice
}
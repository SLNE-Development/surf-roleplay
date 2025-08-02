package dev.slne.surf.job.api.job.requirements

import dev.slne.surf.job.api.job.Job
import dev.slne.surf.job.api.job.JobRegistry
import dev.slne.surf.job.api.job.JobRequirement
import dev.slne.surf.job.api.job.getJob
import dev.slne.surf.job.api.job.jobs.gang.GangsterJob
import dev.slne.surf.roleplay.api.player.RpPlayer

class MinGangsterJobRequirement(val minGangsters: Int) : JobRequirement {
    override fun check(
        job: Job,
        player: RpPlayer
    ) = JobRegistry.getJob<GangsterJob>().players.size >= minGangsters
}
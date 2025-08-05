package dev.slne.surf.job.api.job.requirements

import dev.slne.surf.job.api.job.Job
import dev.slne.surf.job.api.job.JobRegistry
import dev.slne.surf.job.api.job.JobRequirement
import dev.slne.surf.job.api.job.getJob
import dev.slne.surf.job.api.job.jobs.state.MayorJob
import dev.slne.surf.job.api.player.JobPlayer

class HasMayorJobRequirement : JobRequirement({
    line {
        info("Es muss sich ein Bürgermeister im Spiel befinden, um diesem Job beitreten zu können.")
    }
}) {
    override fun check(
        job: Job,
        player: JobPlayer
    ) = JobRegistry.getJob<MayorJob>().players.isNotEmpty()
}
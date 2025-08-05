package dev.slne.surf.job.api.job.requirements

import dev.slne.surf.job.api.job.Job
import dev.slne.surf.job.api.job.JobRequirement
import dev.slne.surf.job.api.player.JobPlayer

object PlayerDoesNotHaveJobRequirement : JobRequirement({
    line {
        info("Du darfst diesem Job nicht erneut beitreten.")
    }
}) {
    override fun check(job: Job, player: JobPlayer) =
        player.currentJob != job
}
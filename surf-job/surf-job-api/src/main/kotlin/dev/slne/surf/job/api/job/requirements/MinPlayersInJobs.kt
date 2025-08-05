package dev.slne.surf.job.api.job.requirements

import dev.slne.surf.job.api.job.Job
import dev.slne.surf.job.api.job.JobRequirement
import dev.slne.surf.job.api.player.JobPlayer
import it.unimi.dsi.fastutil.objects.ObjectSet

class MinPlayersInJobs(val jobs: ObjectSet<Job>) : JobRequirement({
    line {
        info("Es muss sich mindestens ein Spieler in einem der folgenden Jobs befinden:")
    }

    jobs.forEach { job ->
        line {
            spacer("— ")
            append(job)
        }
    }
}) {
    override fun check(job: Job, player: JobPlayer) =
        jobs.isEmpty() || jobs.any {
            it.players.isNotEmpty()
        }
}
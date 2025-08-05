package dev.slne.surf.job.api.job.requirements

import dev.slne.surf.job.api.job.Job
import dev.slne.surf.job.api.job.JobRequirement
import dev.slne.surf.job.api.player.JobPlayer

class MinJobPlayersRequirement(
    val job: Job,
    val minAmount: Int
) : JobRequirement({
    line {
        info("Es müssen mindestens ")
        variableValue(minAmount)
        appendSpace()
        append(job)
        info(" im Spiel sein, um diesem Job beitreten zu können.")
    }
}) {
    override fun check(
        job: Job,
        player: JobPlayer
    ) = job.players.size >= minAmount
}
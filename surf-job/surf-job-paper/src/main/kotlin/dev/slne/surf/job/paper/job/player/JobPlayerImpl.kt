package dev.slne.surf.job.paper.job.player

import dev.slne.surf.job.api.job.Job
import dev.slne.surf.job.api.player.JobPlayer
import dev.slne.surf.roleplay.api.player.RpPlayer

class JobPlayerImpl(
    override val rpPlayer: RpPlayer,
    override var currentJob: Job
) : JobPlayer {
    override fun changeJob(job: Job) = job.assignPlayer(this)
}

suspend fun RpPlayer.jobPlayer() = JobPlayerService[uuid]
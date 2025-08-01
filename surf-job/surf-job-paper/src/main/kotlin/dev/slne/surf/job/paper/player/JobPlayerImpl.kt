package dev.slne.surf.job.paper.player

import dev.slne.surf.job.api.job.Job
import dev.slne.surf.job.api.job.JobRegistry
import dev.slne.surf.job.api.job.getJob
import dev.slne.surf.job.api.job.jobs.neutral.CitizenJob
import dev.slne.surf.job.api.player.JobPlayer
import dev.slne.surf.surfapi.bukkit.api.extensions.server
import java.util.*

class JobPlayerImpl(
    override val uuid: UUID
) : JobPlayer {
    override val bukkitOfflinePlayer get() = server.getOfflinePlayer(uuid)
    override val bukkitPlayer get() = bukkitOfflinePlayer.player

    override var currentJob: Job = JobRegistry.getJob<CitizenJob>()

    override fun changeJob(job: Job): Boolean {
        val canJoin = job.canJoin(this)

        return canJoin
    }
}
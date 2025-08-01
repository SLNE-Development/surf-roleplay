package dev.slne.surf.job.paper.job

import com.google.auto.service.AutoService
import dev.slne.surf.job.api.job.Job
import dev.slne.surf.job.api.job.JobRegistry
import dev.slne.surf.job.api.job.jobs.neutral.CitizenJob
import dev.slne.surf.job.api.player.changeJob
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import net.kyori.adventure.util.Services

@AutoService(JobRegistry::class)
class JobRegistryImpl : JobRegistry, Services.Fallback {

    private val _jobs = mutableObjectSetOf<Job>()
    override val jobs get() = _jobs.freeze()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Job> getJob(jobClass: Class<T>) =
        _jobs.firstOrNull { it::class.java == jobClass } as? T
            ?: throw IllegalArgumentException("Job of class ${jobClass.name} is not registered in the JobRegistry.")

    override fun checkJobKeepRequirements(): Boolean {
        var state = true

        _jobs.forEach { job ->
            job.players.forEach { player ->
                if (!job.canKeep(player)) {
                    player.changeJob<CitizenJob>()
                    state = false
                }
            }
        }

        return state
    }
}
package dev.slne.surf.roleplay.mechanic.mechanics.jobwages

import dev.slne.surf.roleplay.api.mechanic.jobwages.JobWagesMechanic
import dev.slne.surf.roleplay.mechanic.MechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanics.jobwages.listeners.JobWagesHandler
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import kotlin.time.Duration.Companion.seconds

object JobWagesMechanicImpl : MechanicImpl(
    "JobWagesMechanic",
    handlers = objectSetOf(
        JobWagesHandler
    )
), JobWagesMechanic {

    lateinit var jobWagesChecker: JobWagesJob
        private set

    override suspend fun onEnable() {
        jobWagesChecker = JobWagesJob(
            delay = 1.seconds,
            plugin = plugin
        )
        jobWagesChecker.start()
    }

    override suspend fun onDisable() {
        jobWagesChecker.stop()
    }

}
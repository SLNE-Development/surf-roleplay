package dev.slne.surf.job.paper.job.jobs.state

import dev.slne.surf.job.api.job.JobCategory
import dev.slne.surf.job.api.job.JobRegistry
import dev.slne.surf.job.api.job.getJob
import dev.slne.surf.job.api.job.jobs.state.DispatchJob
import dev.slne.surf.job.api.job.jobs.state.FirefighterJob
import dev.slne.surf.job.api.job.jobs.state.PoliceJob
import dev.slne.surf.job.api.job.jobs.state.RescueServiceJob
import dev.slne.surf.job.api.job.requirements.MinPlayersInJobs
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object DispatchJobImpl : JobImpl(
    category = JobCategory.STATE,
    name = "dispatch",
    displayName = buildText { primary("Leitstelle") },
    description = { },
    rules = { },
    income = 300,
    maxPlayers = 2,
    joinRequirements = objectSetOf(
        MinPlayersInJobs(
            objectSetOf(
                JobRegistry.getJob<PoliceJob>(),
                JobRegistry.getJob<FirefighterJob>(),
                JobRegistry.getJob<RescueServiceJob>(),
            )
        )
    )
), DispatchJob
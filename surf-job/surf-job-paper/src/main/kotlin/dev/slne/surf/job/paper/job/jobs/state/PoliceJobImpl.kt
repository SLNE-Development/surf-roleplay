package dev.slne.surf.job.paper.job.jobs.state

import dev.slne.surf.job.api.job.JobCategory
import dev.slne.surf.job.api.job.JobRegistry
import dev.slne.surf.job.api.job.getJob
import dev.slne.surf.job.api.job.jobs.state.PoliceJob
import dev.slne.surf.job.api.job.requirements.MinJobPlayersRequirement
import dev.slne.surf.job.api.job.requirements.MinPlayersJobRequirement
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object PoliceJobImpl : JobImpl(
    category = JobCategory.STATE,
    name = "police",
    displayName = buildText { primary("Polizei") },
    description = { },
    rules = { },
    income = 200,
    maxPlayers = 8,
    joinRequirements = objectSetOf(
        MinPlayersJobRequirement(4)
    )
), PoliceJob {
    object SergeantJobImpl : JobImpl(
        category = JobCategory.STATE,
        name = "police_sergeant",
        displayName = buildText { primary("Polizei Sergeant") },
        description = { },
        rules = { },
        income = 300,
        maxPlayers = 1,
        joinRequirements = objectSetOf(
            MinJobPlayersRequirement(JobRegistry.getJob<PoliceJob>(), 4),
        ),
        keepRequirements = objectSetOf(
            MinJobPlayersRequirement(JobRegistry.getJob<PoliceJob>(), 3),
        )
    ), PoliceJob.SergeantJob
}
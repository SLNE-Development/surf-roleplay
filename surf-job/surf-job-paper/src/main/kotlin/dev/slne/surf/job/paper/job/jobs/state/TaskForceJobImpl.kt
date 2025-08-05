package dev.slne.surf.job.paper.job.jobs.state

import dev.slne.surf.job.api.job.JobCategory
import dev.slne.surf.job.api.job.JobRegistry
import dev.slne.surf.job.api.job.getJob
import dev.slne.surf.job.api.job.jobs.state.PoliceJob
import dev.slne.surf.job.api.job.jobs.state.TaskForceJob
import dev.slne.surf.job.api.job.requirements.MinJobPlayersRequirement
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object TaskForceJobImpl : JobImpl(
    category = JobCategory.STATE,
    name = "task_force",
    displayName = buildText { primary("Sondereinsatzkommando") },
    description = { },
    rules = { },
    income = 250,
    maxPlayers = 8,
    joinRequirements = objectSetOf(
        MinJobPlayersRequirement(JobRegistry.getJob<PoliceJob>(), 4),
    )
), TaskForceJob {
    object TaskForceLeaderImpl : JobImpl(
        category = JobCategory.STATE,
        name = "task_force_leader",
        displayName = buildText { primary("Sondereinsatzkommando Leitung") },
        description = { },
        rules = { },
        income = 300,
        maxPlayers = 1,
        joinRequirements = objectSetOf(
            MinJobPlayersRequirement(JobRegistry.getJob<TaskForceJob>(), 4)
        ),
    ), TaskForceJob.TaskForceLeaderJob
}
package dev.slne.surf.job.paper.job.jobs.state

import dev.slne.surf.job.api.job.jobs.state.TaskForceJob
import dev.slne.surf.job.api.job.requirements.MinPlayersJobRequirement
import dev.slne.surf.job.api.job.requirements.MinPoliceJobRequirement
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object TaskForceJobImpl : JobImpl(
    name = "task_force",
    displayName = buildText { primary("Sondereinsatzkommando") },
    income = 250,
    maxPlayers = 8,
), TaskForceJob {
    object TaskForceLeaderImpl : JobImpl(
        name = "task_force_leader",
        displayName = buildText { primary("Sondereinsatzkommando Leitung") },
        income = 300,
        maxPlayers = 1,
        joinRequirements = objectSetOf(
            MinPlayersJobRequirement(4),
            MinPoliceJobRequirement(8)
        ),
    ), TaskForceJob.TaskForceLeaderJob
}
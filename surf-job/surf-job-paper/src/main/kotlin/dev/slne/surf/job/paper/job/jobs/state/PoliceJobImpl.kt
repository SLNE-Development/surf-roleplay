package dev.slne.surf.job.paper.job.jobs.state

import dev.slne.surf.job.api.job.jobs.state.PoliceJob
import dev.slne.surf.job.api.job.requirements.MinPlayersJobRequirement
import dev.slne.surf.job.api.job.requirements.MinPoliceJobRequirement
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object PoliceJobImpl : JobImpl(
    name = "police",
    displayName = buildText { primary("Polizei") },
    income = 200,
    maxPlayers = 8,
    joinRequirements = objectSetOf(
        MinPlayersJobRequirement(4)
    )
), PoliceJob {
    object SergeantJobImpl : JobImpl(
        name = "police_sergeant",
        displayName = buildText { primary("Polizei Sergeant") },
        income = 300,
        maxPlayers = 1,
        joinRequirements = objectSetOf(
            MinPoliceJobRequirement(8)
        ),
        keepRequirements = objectSetOf(
            MinPoliceJobRequirement(7)
        )
    ), PoliceJob.SergeantJob
}
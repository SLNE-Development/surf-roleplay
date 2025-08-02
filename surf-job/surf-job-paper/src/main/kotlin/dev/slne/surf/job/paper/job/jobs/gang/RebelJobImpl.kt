package dev.slne.surf.job.paper.job.jobs.gang

import dev.slne.surf.job.api.job.jobs.gang.RebelJob
import dev.slne.surf.job.api.job.requirements.MinRebelJobRequirement
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object RebelJobImpl : JobImpl(
    name = "rebel",
    displayName = buildText { primary("Rebell") },
    income = 40,
    maxPlayers = 1,
), RebelJob {
    object RebelChiefJobImpl : JobImpl(
        name = "rebel_chief",
        displayName = buildText { primary("Rebellenboss") },
        income = 75,
        maxPlayers = 1,
        joinRequirements = objectSetOf(
            MinRebelJobRequirement(2)
        ),
        keepRequirements = objectSetOf(
            MinRebelJobRequirement(1)
        )
    ), RebelJob.RebelChiefJob
}
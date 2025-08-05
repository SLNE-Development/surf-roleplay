package dev.slne.surf.job.paper.job.jobs.gang

import dev.slne.surf.job.api.job.JobCategory
import dev.slne.surf.job.api.job.jobs.gang.RebelJob
import dev.slne.surf.job.api.job.requirements.MinJobPlayersRequirement
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object RebelJobImpl : JobImpl(
    category = JobCategory.GANG,
    name = "rebel",
    displayName = buildText { primary("Rebell") },
    description = { },
    rules = { },
    income = 40,
    maxPlayers = 8,
), RebelJob {
    object RebelChiefJobImpl : JobImpl(
        category = JobCategory.GANG,
        name = "rebel_chief",
        displayName = buildText { primary("Rebellenboss") },
        description = { },
        rules = { },
        income = 75,
        maxPlayers = 1,
        joinRequirements = objectSetOf(
            MinJobPlayersRequirement(this, 2)
        ),
        keepRequirements = objectSetOf(
            MinJobPlayersRequirement(this, 1)
        )
    ), RebelJob.RebelChiefJob
}
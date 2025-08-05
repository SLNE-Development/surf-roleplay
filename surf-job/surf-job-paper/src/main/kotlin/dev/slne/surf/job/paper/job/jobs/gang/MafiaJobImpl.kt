package dev.slne.surf.job.paper.job.jobs.gang

import dev.slne.surf.job.api.job.JobCategory
import dev.slne.surf.job.api.job.jobs.gang.MafiaJob
import dev.slne.surf.job.api.job.requirements.MinJobPlayersRequirement
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object MafiaJobImpl : JobImpl(
    category = JobCategory.GANG,
    name = "mafia",
    displayName = buildText { primary("Mafia") },
    description = { },
    rules = { },
    income = 40,
    maxPlayers = 8,
), MafiaJob {
    object MafiaChiefJobImpl : JobImpl(
        category = JobCategory.GANG,
        name = "mafia_chief",
        displayName = buildText { primary("Mafiaboss") },
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
    ), MafiaJob.MafiaChiefJob
}
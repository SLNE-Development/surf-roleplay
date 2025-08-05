package dev.slne.surf.job.paper.job.jobs.gang

import dev.slne.surf.job.api.job.JobCategory
import dev.slne.surf.job.api.job.jobs.gang.GangsterJob
import dev.slne.surf.job.api.job.requirements.MinJobPlayersRequirement
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object GangsterJobImpl : JobImpl(
    category = JobCategory.GANG,
    name = "gangster",
    displayName = buildText { primary("Gangster") },
    description = { },
    rules = { },
    income = 40,
    maxPlayers = 8,
), GangsterJob {
    object GangsterChiefJobImpl : JobImpl(
        category = JobCategory.GANG,
        name = "gangster_chief",
        displayName = buildText { primary("Gangsterboss") },
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
    ), GangsterJob.GangsterChiefJob
}
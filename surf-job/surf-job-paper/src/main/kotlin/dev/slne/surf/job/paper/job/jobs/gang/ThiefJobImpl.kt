package dev.slne.surf.job.paper.job.jobs.gang

import dev.slne.surf.job.api.job.JobCategory
import dev.slne.surf.job.api.job.jobs.gang.ThiefJob
import dev.slne.surf.job.api.job.requirements.MinJobPlayersRequirement
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object ThiefJobImpl : JobImpl(
    category = JobCategory.GANG,
    name = "thief",
    displayName = buildText { primary("Dieb") },
    description = { },
    rules = { },
    income = 40,
    maxPlayers = 8,
), ThiefJob {
    object ThiefChiefJobImpl : JobImpl(
        category = JobCategory.GANG,
        name = "thief_chief",
        displayName = buildText { primary("Meisterdieb") },
        description = { },
        rules = { },
        income = 75,
        maxPlayers = 3,
        joinRequirements = objectSetOf(
            MinJobPlayersRequirement(this, 2)
        ),
        keepRequirements = objectSetOf(
            MinJobPlayersRequirement(this, 1)
        )
    ), ThiefJob.ThiefChiefJob
}
package dev.slne.surf.job.paper.job.jobs.state

import dev.slne.surf.job.api.job.JobCategory
import dev.slne.surf.job.api.job.JobRegistry
import dev.slne.surf.job.api.job.getJob
import dev.slne.surf.job.api.job.jobs.state.FirefighterJob
import dev.slne.surf.job.api.job.requirements.MinJobPlayersRequirement
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object FirefighterJobImpl : JobImpl(
    category = JobCategory.STATE,
    name = "fire_fighter",
    displayName = buildText { primary("Feuerwehr") },
    description = { },
    rules = { },
    income = 280,
    maxPlayers = 8,
), FirefighterJob {
    object FirefighterChiefJobImpl : JobImpl(
        category = JobCategory.STATE,
        name = "fire_fighter_chief",
        displayName = buildText { primary("Leiter der Feuerwehr") },
        description = { },
        rules = { },
        income = 400,
        maxPlayers = 1,
        joinRequirements = objectSetOf(
            MinJobPlayersRequirement(JobRegistry.getJob<FirefighterJob>(), 4)
        ),
        keepRequirements = objectSetOf(
            MinJobPlayersRequirement(JobRegistry.getJob<FirefighterJob>(), 3)
        )
    ), FirefighterJob.FirefighterChiefJob
}
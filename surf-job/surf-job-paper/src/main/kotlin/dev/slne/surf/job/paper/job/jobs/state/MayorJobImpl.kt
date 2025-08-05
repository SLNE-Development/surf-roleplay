package dev.slne.surf.job.paper.job.jobs.state

import dev.slne.surf.job.api.job.JobCategory
import dev.slne.surf.job.api.job.jobs.state.MayorJob
import dev.slne.surf.job.api.job.requirements.MinPlayersJobRequirement
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object MayorJobImpl : JobImpl(
    category = JobCategory.STATE,
    name = "mayor",
    displayName = buildText { primary("Bürgermeister") },
    description = { },
    rules = { },
    income = 2000,
    maxPlayers = 1,
    joinRequirements = objectSetOf(
        MinPlayersJobRequirement(10)
    )
), MayorJob
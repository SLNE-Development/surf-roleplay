package dev.slne.surf.job.paper.job.jobs.neutral

import dev.slne.surf.job.api.job.JobCategory
import dev.slne.surf.job.api.job.jobs.neutral.DjJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object DjJobImpl : JobImpl(
    category = JobCategory.NEUTRAL,
    name = "dj",
    displayName = buildText { primary("DJ") },
    description = { },
    rules = { },
    income = 0,
    maxPlayers = 1
), DjJob
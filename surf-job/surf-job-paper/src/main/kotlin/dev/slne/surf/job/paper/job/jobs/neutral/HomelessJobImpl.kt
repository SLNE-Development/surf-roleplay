package dev.slne.surf.job.paper.job.jobs.neutral

import dev.slne.surf.job.api.job.JobCategory
import dev.slne.surf.job.api.job.jobs.neutral.HomelessJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object HomelessJobImpl : JobImpl(
    category = JobCategory.NEUTRAL,
    name = "homeless",
    displayName = buildText { primary("Obdachloser") },
    description = { },
    rules = { },
    income = 0,
    maxPlayers = -1
), HomelessJob
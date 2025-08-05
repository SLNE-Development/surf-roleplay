package dev.slne.surf.job.paper.job.jobs.neutral

import dev.slne.surf.job.api.job.JobCategory
import dev.slne.surf.job.api.job.jobs.neutral.CitizenJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object CitizenJobImpl : JobImpl(
    category = JobCategory.NEUTRAL,
    name = "citizen",
    displayName = buildText { primary("Bürger") },
    description = { },
    rules = { },
    income = 100,
    maxPlayers = -1
), CitizenJob
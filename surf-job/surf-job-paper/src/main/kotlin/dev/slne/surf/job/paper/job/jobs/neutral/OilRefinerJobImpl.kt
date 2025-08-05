package dev.slne.surf.job.paper.job.jobs.neutral

import dev.slne.surf.job.api.job.JobCategory
import dev.slne.surf.job.api.job.jobs.neutral.OilRefinerJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object OilRefinerJobImpl : JobImpl(
    category = JobCategory.NEUTRAL,
    name = "oil_refiner",
    displayName = buildText { primary("Ölraffinerist") },
    description = { },
    rules = { },
    income = 0,
    maxPlayers = 4
), OilRefinerJob
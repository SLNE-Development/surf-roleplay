package dev.slne.surf.job.paper.job.jobs.neutral

import dev.slne.surf.job.api.job.JobCategory
import dev.slne.surf.job.api.job.jobs.neutral.TaxiDriverJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object TaxiDriverJobImpl : JobImpl(
    category = JobCategory.NEUTRAL,
    name = "taxi_driver",
    displayName = buildText { primary("Taxifahrer") },
    description = { },
    rules = { },
    income = 0,
    maxPlayers = 2
), TaxiDriverJob
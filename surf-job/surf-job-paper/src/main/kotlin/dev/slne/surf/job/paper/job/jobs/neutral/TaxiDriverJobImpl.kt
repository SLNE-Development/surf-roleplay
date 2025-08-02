package dev.slne.surf.job.paper.job.jobs.neutral

import dev.slne.surf.job.api.job.jobs.neutral.TaxiDriverJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object TaxiDriverJobImpl : JobImpl(
    name = "taxi_driver",
    displayName = buildText { primary("Taxifahrer") },
    income = 0,
    maxPlayers = -1
), TaxiDriverJob
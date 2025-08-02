package dev.slne.surf.job.paper.job.jobs.neutral

import dev.slne.surf.job.api.job.jobs.neutral.OilRefinerJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object OilRefinerJobImpl : JobImpl(
    name = "oil_refiner",
    displayName = buildText { primary("Erdölraffinerist") },
    income = 0,
    maxPlayers = -1
), OilRefinerJob
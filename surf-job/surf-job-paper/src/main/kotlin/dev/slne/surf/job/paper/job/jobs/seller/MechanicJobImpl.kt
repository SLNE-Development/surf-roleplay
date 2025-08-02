package dev.slne.surf.job.paper.job.jobs.seller

import dev.slne.surf.job.api.job.jobs.seller.MechanicJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object MechanicJobImpl : JobImpl(
    name = "mechanic",
    displayName = buildText { primary("Mechaniker") },
    income = 340,
    maxPlayers = 1,
), MechanicJob
package dev.slne.surf.job.paper.job.jobs.seller

import dev.slne.surf.job.api.job.JobCategory
import dev.slne.surf.job.api.job.jobs.seller.MechanicJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object MechanicJobImpl : JobImpl(
    category = JobCategory.SELLER,
    name = "mechanic",
    displayName = buildText { primary("Mechaniker") },
    description = { },
    rules = { },
    income = 340,
    maxPlayers = 2,
), MechanicJob
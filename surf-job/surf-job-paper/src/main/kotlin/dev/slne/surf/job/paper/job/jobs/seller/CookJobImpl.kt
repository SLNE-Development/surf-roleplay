package dev.slne.surf.job.paper.job.jobs.seller

import dev.slne.surf.job.api.job.JobCategory
import dev.slne.surf.job.api.job.jobs.seller.CookJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object CookJobImpl : JobImpl(
    category = JobCategory.SELLER,
    name = "cook",
    displayName = buildText { primary("Koch") },
    description = { },
    rules = { },
    income = 340,
    maxPlayers = 2,
), CookJob
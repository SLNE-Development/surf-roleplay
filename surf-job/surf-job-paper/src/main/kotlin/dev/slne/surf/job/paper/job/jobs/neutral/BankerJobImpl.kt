package dev.slne.surf.job.paper.job.jobs.neutral

import dev.slne.surf.job.api.job.JobCategory
import dev.slne.surf.job.api.job.jobs.neutral.BankerJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object BankerJobImpl : JobImpl(
    category = JobCategory.NEUTRAL,
    name = "banker",
    displayName = buildText { primary("Banker") },
    description = { },
    rules = { },
    income = 0,
    maxPlayers = 4
), BankerJob
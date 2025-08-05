package dev.slne.surf.job.paper.job.jobs.neutral

import dev.slne.surf.job.api.job.JobCategory
import dev.slne.surf.job.api.job.jobs.neutral.MinerJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object MinerJobImpl : JobImpl(
    category = JobCategory.NEUTRAL,
    name = "miner",
    displayName = buildText { primary("Bergmann") },
    description = { },
    rules = { },
    income = 0,
    maxPlayers = 8
), MinerJob
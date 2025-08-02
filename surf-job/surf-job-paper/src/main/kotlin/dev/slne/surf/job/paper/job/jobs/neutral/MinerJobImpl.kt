package dev.slne.surf.job.paper.job.jobs.neutral

import dev.slne.surf.job.api.job.jobs.neutral.MinerJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object MinerJobImpl : JobImpl(
    name = "miner",
    displayName = buildText { primary("Bergmann") },
    income = 0,
    maxPlayers = -1
), MinerJob
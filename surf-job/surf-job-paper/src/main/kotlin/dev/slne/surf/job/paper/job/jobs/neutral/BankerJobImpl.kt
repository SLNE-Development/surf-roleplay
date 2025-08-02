package dev.slne.surf.job.paper.job.jobs.neutral

import dev.slne.surf.job.api.job.jobs.neutral.BankerJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object BankerJobImpl : JobImpl(
    name = "banker",
    displayName = buildText { primary("Bänker") },
    income = 0,
    maxPlayers = -1
), BankerJob
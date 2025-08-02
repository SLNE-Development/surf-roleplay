package dev.slne.surf.job.paper.job.jobs.neutral

import dev.slne.surf.job.api.job.jobs.neutral.DiscJockeyJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object DiscJockeyJobImpl : JobImpl(
    name = "dj",
    displayName = buildText { primary("DJ") },
    income = 0,
    maxPlayers = -1
), DiscJockeyJob
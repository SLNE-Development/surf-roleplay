package dev.slne.surf.job.paper.job.jobs.neutral

import dev.slne.surf.job.api.job.jobs.neutral.CitizenJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object CitizenJobImpl : JobImpl(
    name = "citizen",
    displayName = buildText { primary("Bürger") },
    income = 0,
    maxPlayers = -1
), CitizenJob
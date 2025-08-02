package dev.slne.surf.job.paper.job.jobs.neutral

import dev.slne.surf.job.api.job.jobs.neutral.HomelessJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object HomelessJobImpl : JobImpl(
    name = "homeless",
    displayName = buildText { primary("Obdachloser") },
    income = 0,
    maxPlayers = -1
), HomelessJob
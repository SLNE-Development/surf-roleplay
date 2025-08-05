package dev.slne.surf.job.paper.job.jobs.gang

import dev.slne.surf.job.api.job.JobCategory
import dev.slne.surf.job.api.job.jobs.gang.TerroristJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object TerroristJobImpl : JobImpl(
    category = JobCategory.GANG,
    name = "terrorist",
    displayName = buildText { primary("Terrorist") },
    description = { },
    rules = { },
    income = 40,
    maxPlayers = 2,
), TerroristJob
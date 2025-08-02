package dev.slne.surf.job.paper.job.jobs.gang

import dev.slne.surf.job.api.job.jobs.gang.TerroristJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object TerroristJobImpl : JobImpl(
    name = "terrorist",
    displayName = buildText { primary("Terrorist") },
    income = 40,
    maxPlayers = 1,
), TerroristJob
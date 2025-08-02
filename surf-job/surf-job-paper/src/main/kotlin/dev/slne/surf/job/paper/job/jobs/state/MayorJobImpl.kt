package dev.slne.surf.job.paper.job.jobs.state

import dev.slne.surf.job.api.job.jobs.state.MayorJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object MayorJobImpl : JobImpl(
    name = "mayor",
    displayName = buildText { primary("Bürgermeister") },
    income = 2000,
    maxPlayers = 1,
), MayorJob
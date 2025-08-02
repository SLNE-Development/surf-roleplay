package dev.slne.surf.job.paper.job.jobs.state

import dev.slne.surf.job.api.job.jobs.state.DispatchJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object DispatchJobImpl : JobImpl(
    name = "dispatch",
    displayName = buildText { primary("Leitstelle") },
    income = 300,
    maxPlayers = 2
), DispatchJob
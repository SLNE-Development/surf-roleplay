package dev.slne.surf.job.paper.job.jobs.state

import dev.slne.surf.job.api.job.JobCategory
import dev.slne.surf.job.api.job.jobs.state.RescueServiceJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object RescueServiceJobImpl : JobImpl(
    category = JobCategory.STATE,
    name = "rescue_service",
    displayName = buildText { primary("Rettungsdienst") },
    description = { },
    rules = { },
    income = 300,
    maxPlayers = 2,
), RescueServiceJob
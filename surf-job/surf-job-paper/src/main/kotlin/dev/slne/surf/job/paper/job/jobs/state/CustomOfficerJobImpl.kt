package dev.slne.surf.job.paper.job.jobs.state

import dev.slne.surf.job.api.job.JobCategory
import dev.slne.surf.job.api.job.jobs.state.CustomOfficerJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object CustomOfficerJobImpl : JobImpl(
    category = JobCategory.STATE,
    name = "custom_officer",
    displayName = buildText { primary("Zollbeamter") },
    description = { },
    rules = { },
    income = 340,
    maxPlayers = 4,
), CustomOfficerJob
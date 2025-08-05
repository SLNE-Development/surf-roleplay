package dev.slne.surf.job.paper.job.jobs.neutral

import dev.slne.surf.job.api.job.JobCategory
import dev.slne.surf.job.api.job.jobs.neutral.LawyerJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object LawyerJobImpl : JobImpl(
    category = JobCategory.NEUTRAL,
    name = "lawyer",
    displayName = buildText { primary("Anwalt") },
    description = { },
    rules = { },
    income = 0,
    maxPlayers = 2
), LawyerJob
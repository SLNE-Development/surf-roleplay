package dev.slne.surf.job.paper.job.jobs.neutral

import dev.slne.surf.job.api.job.jobs.neutral.LawyerJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object LawyerJobImpl : JobImpl(
    name = "lawyer",
    displayName = buildText { primary("Anwalt") },
    income = 0,
    maxPlayers = -1
), LawyerJob
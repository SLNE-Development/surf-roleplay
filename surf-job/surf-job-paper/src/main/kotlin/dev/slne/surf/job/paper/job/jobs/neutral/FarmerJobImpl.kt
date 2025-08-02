package dev.slne.surf.job.paper.job.jobs.neutral

import dev.slne.surf.job.api.job.jobs.neutral.FarmerJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object FarmerJobImpl : JobImpl(
    name = "farmer",
    displayName = buildText { primary("Bauer") },
    income = 0,
    maxPlayers = -1
), FarmerJob
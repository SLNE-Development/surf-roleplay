package dev.slne.surf.job.paper.job.jobs.neutral

import dev.slne.surf.job.api.job.jobs.neutral.PriestJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object PriestJobImpl : JobImpl(
    name = "priest",
    displayName = buildText { primary("Priester") },
    income = 0,
    maxPlayers = -1
), PriestJob
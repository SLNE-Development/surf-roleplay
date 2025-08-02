package dev.slne.surf.job.paper.job.jobs.neutral

import dev.slne.surf.job.api.job.jobs.neutral.SecurityJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object SecurityJobImpl : JobImpl(
    name = "security",
    displayName = buildText { primary("Sicherheitsmann") },
    income = 0,
    maxPlayers = -1
), SecurityJob
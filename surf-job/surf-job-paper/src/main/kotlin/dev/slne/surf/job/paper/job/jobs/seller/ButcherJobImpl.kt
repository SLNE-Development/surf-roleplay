package dev.slne.surf.job.paper.job.jobs.seller

import dev.slne.surf.job.api.job.jobs.seller.ButcherJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object ButcherJobImpl : JobImpl(
    name = "butcher",
    displayName = buildText { primary("Metzger") },
    income = 340,
    maxPlayers = 1,
), ButcherJob
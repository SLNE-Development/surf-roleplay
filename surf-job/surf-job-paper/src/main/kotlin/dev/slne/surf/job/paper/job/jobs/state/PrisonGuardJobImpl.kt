package dev.slne.surf.job.paper.job.jobs.state

import dev.slne.surf.job.api.job.JobCategory
import dev.slne.surf.job.api.job.JobRegistry
import dev.slne.surf.job.api.job.getJob
import dev.slne.surf.job.api.job.jobs.state.PoliceJob
import dev.slne.surf.job.api.job.jobs.state.PrisonGuardJob
import dev.slne.surf.job.api.job.requirements.MinJobPlayersRequirement
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object PrisonGuardJobImpl : JobImpl(
    category = JobCategory.STATE,
    name = "prison_guard",
    displayName = buildText { primary("Gefängniswärter") },
    description = { },
    rules = { },
    income = 400,
    maxPlayers = 2,
    joinRequirements = objectSetOf(
        MinJobPlayersRequirement(JobRegistry.getJob<PoliceJob>(), 1)
    )
), PrisonGuardJob
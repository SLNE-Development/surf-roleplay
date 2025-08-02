package dev.slne.surf.job.paper.job.jobs.state

import dev.slne.surf.job.api.job.jobs.state.PrisonGuardJob
import dev.slne.surf.job.api.job.requirements.HasPoliceJobRequirement
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object PrisonGuardJobImpl : JobImpl(
    name = "prison_guard",
    displayName = buildText { primary("Gefängniswärter") },
    income = 400,
    maxPlayers = 1,
    joinRequirements = objectSetOf(
        HasPoliceJobRequirement()
    )
), PrisonGuardJob
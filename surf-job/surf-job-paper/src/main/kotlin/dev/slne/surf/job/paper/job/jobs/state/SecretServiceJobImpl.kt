package dev.slne.surf.job.paper.job.jobs.state

import dev.slne.surf.job.api.job.JobCategory
import dev.slne.surf.job.api.job.jobs.state.SecretServiceJob
import dev.slne.surf.job.api.job.requirements.HasMayorJobRequirement
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object SecretServiceJobImpl : JobImpl(
    category = JobCategory.STATE,
    name = "secret_service",
    displayName = buildText { primary("Geheimdienst") },
    description = { },
    rules = { },
    income = 500,
    maxPlayers = 2,
    joinRequirements = objectSetOf(
        HasMayorJobRequirement()
    )
), SecretServiceJob
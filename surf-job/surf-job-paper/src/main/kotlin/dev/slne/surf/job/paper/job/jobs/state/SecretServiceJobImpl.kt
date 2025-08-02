package dev.slne.surf.job.paper.job.jobs.state

import dev.slne.surf.job.api.job.jobs.state.SecretServiceJob
import dev.slne.surf.job.api.job.requirements.HasMayorJobRequirement
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object SecretServiceJobImpl : JobImpl(
    name = "secret_service",
    displayName = buildText { primary("Geheimdienst") },
    income = 500,
    maxPlayers = 8,
    joinRequirements = objectSetOf(
        HasMayorJobRequirement()
    )
), SecretServiceJob
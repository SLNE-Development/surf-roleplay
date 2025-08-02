package dev.slne.surf.job.paper.job.jobs.gang

import dev.slne.surf.job.api.job.jobs.gang.MafiaJob
import dev.slne.surf.job.api.job.requirements.MinMafiaRequirement
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object MafiaJobImpl : JobImpl(
    name = "mafia",
    displayName = buildText { primary("Mafia") },
    income = 40,
    maxPlayers = 1,
), MafiaJob {
    object MafiaChiefJobImpl : JobImpl(
        name = "mafia_chief",
        displayName = buildText { primary("Mafiaboss") },
        income = 75,
        maxPlayers = 1,
        joinRequirements = objectSetOf(
            MinMafiaRequirement(2)
        ),
        keepRequirements = objectSetOf(
            MinMafiaRequirement(1)
        )
    ), MafiaJob.MafiaChiefJob
}
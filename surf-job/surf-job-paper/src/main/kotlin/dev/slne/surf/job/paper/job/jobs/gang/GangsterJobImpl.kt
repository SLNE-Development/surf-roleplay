package dev.slne.surf.job.paper.job.jobs.gang

import dev.slne.surf.job.api.job.jobs.gang.GangsterJob
import dev.slne.surf.job.api.job.requirements.MinGangsterJobRequirement
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object GangsterJobImpl : JobImpl(
    name = "gangster",
    displayName = buildText { primary("Gangster") },
    income = 40,
    maxPlayers = 1,
), GangsterJob {
    object GangsterChiefJobImpl : JobImpl(
        name = "gangster_chief",
        displayName = buildText { primary("Gangsterboss") },
        income = 75,
        maxPlayers = 1,
        joinRequirements = objectSetOf(
            MinGangsterJobRequirement(2)
        ),
        keepRequirements = objectSetOf(
            MinGangsterJobRequirement(1)
        )
    ), GangsterJob.GangsterChiefJob
}
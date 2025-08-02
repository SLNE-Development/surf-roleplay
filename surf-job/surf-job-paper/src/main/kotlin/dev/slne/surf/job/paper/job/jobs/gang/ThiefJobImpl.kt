package dev.slne.surf.job.paper.job.jobs.gang

import dev.slne.surf.job.api.job.jobs.gang.ThiefJob
import dev.slne.surf.job.api.job.requirements.MinTheifJobRequirement
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object ThiefJobImpl : JobImpl(
    name = "thief",
    displayName = buildText { primary("Dieb") },
    income = 40,
    maxPlayers = 1,
), ThiefJob {
    object ThiefChiefJobImpl : JobImpl(
        name = "thief_chief",
        displayName = buildText { primary("Meisterdieb") },
        income = 75,
        maxPlayers = 1,
        joinRequirements = objectSetOf(
            MinTheifJobRequirement(2)
        ),
        keepRequirements = objectSetOf(
            MinTheifJobRequirement(1)
        )
    ), ThiefJob.ThiefChiefJob
}
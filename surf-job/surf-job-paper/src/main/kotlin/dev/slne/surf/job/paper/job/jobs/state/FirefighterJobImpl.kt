package dev.slne.surf.job.paper.job.jobs.state

import dev.slne.surf.job.api.job.jobs.state.FirefighterJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object FirefighterJobImpl : JobImpl(
    name = "fire_fighter",
    displayName = buildText { primary("Feuerwehr") },
    income = 280,
    maxPlayers = 1,
), FirefighterJob {
    object FirefighterChiefJobImpl : JobImpl(
        name = "fire_fighter_chief",
        displayName = buildText { primary("Leiter der Feuerwehr") },
        income = 400,
        maxPlayers = 1,
    ), FirefighterJob.FirefighterChiefJob
}
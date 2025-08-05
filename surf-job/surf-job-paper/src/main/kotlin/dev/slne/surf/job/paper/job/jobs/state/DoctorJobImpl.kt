package dev.slne.surf.job.paper.job.jobs.state

import dev.slne.surf.job.api.job.JobCategory
import dev.slne.surf.job.api.job.jobs.state.DoctorJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object DoctorJobImpl : JobImpl(
    category = JobCategory.STATE,
    name = "doctor",
    displayName = buildText { primary("Arzt") },
    description = { },
    rules = { },
    income = 340,
    maxPlayers = 2,
), DoctorJob
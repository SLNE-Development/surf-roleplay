package dev.slne.surf.job.paper.job.jobs.state

import dev.slne.surf.job.api.job.jobs.state.DoctorJob
import dev.slne.surf.job.paper.job.JobImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object DoctorJobImpl : JobImpl(
    name = "doctor",
    displayName = buildText { primary("Arzt") },
    income = 340,
    maxPlayers = 1,
), DoctorJob
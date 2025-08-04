package dev.slne.surf.roleplay.mechanic.mechanics.jobwages

import dev.slne.surf.roleplay.api.mechanic.jobwages.JobWagesMechanic
import dev.slne.surf.roleplay.mechanic.MechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanics.jobwages.listeners.JobWagesHandler
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object JobWagesMechanicImpl : MechanicImpl(
    "JobWagesMechanic",
    handlers = objectSetOf(
        JobWagesHandler
    ),
    rpJobs = objectSetOf(
        JobWagesJob
    )
), JobWagesMechanic
package dev.slne.surf.job.api.job.events

import dev.slne.surf.job.api.job.Job
import dev.slne.surf.job.api.player.JobPlayer
import dev.slne.surf.roleplay.api.events.CancellableRpEvent

class PlayerJobChangeEvent(
    val player: JobPlayer,
    val oldJob: Job,
    val newJob: Job
) : CancellableRpEvent()
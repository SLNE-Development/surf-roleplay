package dev.slne.surf.job.api.job.jobs.gang

import dev.slne.surf.job.api.job.Job

interface RebelJob : Job {
    interface RebelChiefJob : RebelJob
}
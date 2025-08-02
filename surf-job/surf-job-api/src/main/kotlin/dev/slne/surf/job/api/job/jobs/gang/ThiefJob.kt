package dev.slne.surf.job.api.job.jobs.gang

import dev.slne.surf.job.api.job.Job

interface ThiefJob : Job {
    interface ThiefChiefJob : ThiefJob
}
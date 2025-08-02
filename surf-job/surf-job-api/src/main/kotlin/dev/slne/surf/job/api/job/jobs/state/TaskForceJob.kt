package dev.slne.surf.job.api.job.jobs.state

import dev.slne.surf.job.api.job.Job

interface TaskForceJob : Job {
    interface TaskForceLeaderJob : TaskForceJob
}
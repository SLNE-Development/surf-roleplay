package dev.slne.surf.job.api.job.requirements

import dev.slne.surf.job.api.job.Job
import dev.slne.surf.job.api.job.JobRequirement
import dev.slne.surf.job.api.player.JobPlayer

object HasJobPermissionRequirement : JobRequirement({
    line {
        info("Du hast nicht die nötigen Rechte, um diesen Job zu betreten.")
    }
}) {
    override fun check(job: Job, player: JobPlayer) =
        player.rpPlayer.bukkitPlayer?.hasPermission(job.permission) == true
}
package dev.slne.surf.job.api.job.requirements

import dev.slne.surf.job.api.job.Job
import dev.slne.surf.job.api.job.JobRequirement
import dev.slne.surf.job.api.job.utils.PermissionRegistry
import dev.slne.surf.job.api.player.JobPlayer

class MaxPlayersRequirement(maxAmount: Int) : JobRequirement({
    line {
        info("Es können sich maximal ")
        variableValue(maxAmount)
        info(" Spieler in diesem Job befinden.")
    }
}) {
    override fun check(job: Job, player: JobPlayer): Boolean {
        val jobBypass = job.maxPlayers == -1
        val permissionBypass = player.rpPlayer.bukkitPlayer
            ?.hasPermission(PermissionRegistry.JOB_JOIN_IGNORE_MAX_PLAYERS) == true

        return jobBypass || permissionBypass || job.players.size < job.maxPlayers
    }
}
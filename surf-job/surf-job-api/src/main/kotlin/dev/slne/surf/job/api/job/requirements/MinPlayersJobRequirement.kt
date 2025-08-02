package dev.slne.surf.job.api.job.requirements

import dev.slne.surf.job.api.job.Job
import dev.slne.surf.job.api.job.JobRequirement
import dev.slne.surf.job.api.player.JobPlayer
import dev.slne.surf.surfapi.bukkit.api.extensions.server

class MinPlayersJobRequirement(val minPlayers: Int) : JobRequirement {
    override fun check(
        job: Job,
        player: JobPlayer
    ) = server.onlinePlayers.size >= minPlayers
}
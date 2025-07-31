package dev.slne.surf.job.paper.job

import dev.slne.surf.job.api.job.Job
import dev.slne.surf.job.api.job.JobRequirement
import dev.slne.surf.job.api.player.JobPlayer
import dev.slne.surf.job.api.player.JobPlayerManager
import dev.slne.surf.job.paper.utils.PermissionRegistry
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.text.Component

open class JobImpl(
    override val name: String,
    override val displayName: Component,
    override val income: Int,
    override val maxPlayers: Int,
    override val joinRequirements: ObjectSet<JobRequirement> = mutableObjectSetOf(),
    override val keepRequirements: ObjectSet<JobRequirement> = mutableObjectSetOf()
) : Job {

    init {
        // Default join requirement: job must have fewer players than maxPlayers
        joinRequirements.add { job, player ->
            job.maxPlayers == -1 || job.players.size < job.maxPlayers ||
                    player.bukkitPlayer?.hasPermission(
                        PermissionRegistry.JOB_JOIN_IGNORE_MAX_PLAYERS
                    ) == true
        }
    }

    override val players
        get() = JobPlayerManager.players.filter { it.currentJob == this }.toObjectSet()

    override fun canJoin(player: JobPlayer) = joinRequirements.all { it.check(this, player) }
    override fun canKeep(player: JobPlayer) = keepRequirements.all { it.check(this, player) }

    override fun assignPlayer(player: JobPlayer) {
        if (!canJoin(player)) {
            throw IllegalStateException("Player ${player.uuid} cannot join job $name due to requirements not met.")
        }

        player.currentJob = this
    }
}
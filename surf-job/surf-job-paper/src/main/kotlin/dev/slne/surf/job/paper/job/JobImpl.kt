package dev.slne.surf.job.paper.job

import dev.slne.surf.job.api.job.Job
import dev.slne.surf.job.api.job.JobRequirement
import dev.slne.surf.job.api.job.jobs.neutral.CitizenJob
import dev.slne.surf.job.api.player.changeJob
import dev.slne.surf.job.api.player.currentJob
import dev.slne.surf.job.paper.utils.PermissionRegistry
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
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

    private val _players = mutableObjectSetOf<RpPlayer>()
    override val players get() = _players.freeze()

    override fun canJoin(player: RpPlayer) = joinRequirements.all { it.check(this, player) }
    override fun canKeep(player: RpPlayer) = keepRequirements.all { it.check(this, player) }

    override fun assignPlayer(player: RpPlayer): Boolean {
        if (!canJoin(player)) {
            throw IllegalStateException("Player ${player.uuid} cannot join job $name due to requirements not met.")
        }

        val oldJob = player.currentJob
        oldJob.removePlayer(player)

        _players.add(player)

        return true
    }

    override fun removePlayer(player: RpPlayer): Boolean {
        _players.remove(player)

        if (player.currentJob == this) {
            player.changeJob<CitizenJob>()
        }

        performKeepRequirementsCheck()

        return true
    }

    override fun performKeepRequirementsCheck(): Boolean {
        var state = true

        _players.forEach { player ->
            if (!canKeep(player)) {
                player.changeJob<CitizenJob>()
                state = false
            }
        }

        return state
    }
}
package dev.slne.surf.job.paper.job

import dev.slne.surf.job.api.job.Job
import dev.slne.surf.job.api.job.JobRequirement
import dev.slne.surf.job.api.job.jobs.neutral.CitizenJob
import dev.slne.surf.job.api.player.JobPlayer
import dev.slne.surf.job.api.player.changeJob
import dev.slne.surf.job.paper.job.player.JobPlayerImpl
import dev.slne.surf.job.paper.job.player.JobPlayerService
import dev.slne.surf.job.paper.utils.PermissionRegistry
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.text.Component
import java.text.NumberFormat
import java.util.*

open class JobImpl(
    override val name: String,
    override val displayName: Component,
    override val income: Int,
    override val maxPlayers: Int,
    override val joinRequirements: ObjectSet<JobRequirement> = mutableObjectSetOf(),
    override val keepRequirements: ObjectSet<JobRequirement> = joinRequirements
) : Job {

    private val _finalJoinRequirements = mutableObjectSetOf<JobRequirement>()
    private val _finalKeepRequirements = mutableObjectSetOf<JobRequirement>()

    init {
        _finalJoinRequirements.add { job, player ->
            job.maxPlayers == -1 || job.players.size < job.maxPlayers ||
                    player.rpPlayer.bukkitPlayer?.hasPermission(
                        PermissionRegistry.JOB_JOIN_IGNORE_MAX_PLAYERS
                    ) == true
        }
        _finalJoinRequirements.addAll(joinRequirements)
        _finalKeepRequirements.addAll(keepRequirements)
    }

    override val players get() = JobPlayerService.players.filter { it.currentJob == this }.toObjectSet()

    override fun formatIncome(locale: Locale): String =
        NumberFormat.getNumberInstance(locale).format(income)

    override fun canJoin(player: JobPlayer) = _finalJoinRequirements.all { it.check(this, player) }
    override fun canKeep(player: JobPlayer) = _finalKeepRequirements.all { it.check(this, player) }

    override fun assignPlayer(player: JobPlayer): Boolean {
        val player = player as JobPlayerImpl

        if (!canJoin(player)) {
            throw IllegalStateException("Player ${player.rpPlayer.uuid} cannot join job $name due to requirements not met.")
        }

        val oldJob = player.currentJob
        player.currentJob = this
        oldJob.performKeepRequirementsCheck()

        return true
    }

    override fun removePlayer(player: JobPlayer): Boolean {
        if (player.currentJob == this) {
            player.changeJob<CitizenJob>()
        }

        performKeepRequirementsCheck()

        return true
    }

    override fun performKeepRequirementsCheck(): Boolean {
        var state = true

        players.forEach { player ->
            if (!canKeep(player)) {
                player.changeJob<CitizenJob>()
                state = false
            }
        }

        return state
    }

    override fun toString(): String {
        return "JobImpl(_finalKeepRequirements=$_finalKeepRequirements, _finalJoinRequirements=$_finalJoinRequirements, maxPlayers=$maxPlayers, income=$income, displayName=$displayName, name='$name', players=$players)"
    }

}
package dev.slne.surf.job.paper.job

import dev.slne.surf.job.api.job.Job
import dev.slne.surf.job.api.job.JobCategory
import dev.slne.surf.job.api.job.JobRequirement
import dev.slne.surf.job.api.job.jobs.neutral.CitizenJob
import dev.slne.surf.job.api.job.requirements.HasJobPermissionRequirement
import dev.slne.surf.job.api.job.requirements.MaxPlayersRequirement
import dev.slne.surf.job.api.job.requirements.PlayerDoesNotHaveJobRequirement
import dev.slne.surf.job.api.job.utils.PermissionRegistry
import dev.slne.surf.job.api.player.JobPlayer
import dev.slne.surf.job.api.player.changeJob
import dev.slne.surf.job.paper.job.player.JobPlayerImpl
import dev.slne.surf.job.paper.job.player.JobPlayerService
import dev.slne.surf.surfapi.bukkit.api.builder.LoreBuilder
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.toObjectList
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.text.Component
import java.text.NumberFormat
import java.util.*

open class JobImpl(
    override val category: JobCategory,
    override val name: String,
    override val displayName: Component,
    override val description: LoreBuilder.() -> Unit,
    override val rules: LoreBuilder.() -> Unit,
    override val income: Int,
    override val maxPlayers: Int,
    override val joinRequirements: ObjectSet<JobRequirement> = mutableObjectSetOf(),
    override val keepRequirements: ObjectSet<JobRequirement> = joinRequirements,
) : Job {

    override val permission: String = PermissionRegistry.createJobJoinPermission(this)

    private val _finalJoinRequirements = mutableObjectSetOf<JobRequirement>()
    private val _finalKeepRequirements = mutableObjectSetOf<JobRequirement>()

    init {
        _finalJoinRequirements.add(MaxPlayersRequirement(maxPlayers))
        _finalJoinRequirements.add(HasJobPermissionRequirement)
        _finalJoinRequirements.add(PlayerDoesNotHaveJobRequirement)
        _finalJoinRequirements.addAll(joinRequirements)

        _finalKeepRequirements.add(MaxPlayersRequirement(maxPlayers))
        _finalKeepRequirements.add(HasJobPermissionRequirement)
        _finalKeepRequirements.addAll(keepRequirements)
    }

    override val players
        get() = JobPlayerService.players.filter { it.currentJob == this }.toObjectSet()

    override fun formatIncome(locale: Locale?): String =
        "${NumberFormat.getNumberInstance(locale ?: Locale.GERMAN).format(income)} €"

    override fun canJoin(player: JobPlayer) =
        _finalJoinRequirements.filter { !it.check(this, player) }.toObjectList()

    override fun canKeep(player: JobPlayer) =
        _finalKeepRequirements.filter { !it.check(this, player) }.toObjectList()

    override fun assignPlayer(player: JobPlayer): Job.JobChangeResult {
        val player = player as JobPlayerImpl

        if (player.currentJob == this) {
            return Job.JobChangeResult.AlreadyInJob
        }

        val joinRequirements = canJoin(player)
        if (joinRequirements.isNotEmpty()) {
            return Job.JobChangeResult.JoinRequirementsNotMet(joinRequirements)
        }

        val oldJob = player.currentJob
        player.currentJob = this
        oldJob.performKeepRequirementsCheck()

        return Job.JobChangeResult.Success
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
            if (canKeep(player).isNotEmpty()) {
                player.changeJob<CitizenJob>()
                state = false
            }
        }

        return state
    }

    override fun toString(): String {
        return "JobImpl(_finalKeepRequirements=$_finalKeepRequirements, _finalJoinRequirements=$_finalJoinRequirements, maxPlayers=$maxPlayers, income=$income, displayName=$displayName, name='$name', players=$players)"
    }

    override fun asComponent() = displayName

}
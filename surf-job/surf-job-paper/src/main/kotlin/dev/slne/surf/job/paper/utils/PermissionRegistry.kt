package dev.slne.surf.job.paper.utils

import dev.slne.surf.job.api.job.Job
import dev.slne.surf.job.api.job.JobRegistry
import dev.slne.surf.surfapi.bukkit.api.permission.PermissionRegistry

object PermissionRegistry : PermissionRegistry() {

    private const val PREFIX = "surf.roleplay.job"
    private const val COMMAND_PREFIX = "$PREFIX.command"

    val JOB_JOIN_IGNORE_MAX_PLAYERS = create("$PREFIX.join.ignore-max-players")

    private fun createJobJoinPermission(job: Job) =
        create("$COMMAND_PREFIX.join.${job.name.lowercase().replace(" ", "-")}")

    fun createJobJoinPermissions() = JobRegistry.jobs.forEach {
        createJobJoinPermission(it)
    }

}
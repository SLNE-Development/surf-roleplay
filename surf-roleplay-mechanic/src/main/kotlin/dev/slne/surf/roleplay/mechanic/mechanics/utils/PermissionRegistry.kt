package dev.slne.surf.roleplay.mechanic.mechanics.utils

import dev.slne.surf.surfapi.bukkit.api.permission.PermissionRegistry

object PermissionRegistry : PermissionRegistry() {

    const val PREFIX = "surf.roleplay.mechanic"
    private const val COMMAND_PREFIX = "$PREFIX.command"

}
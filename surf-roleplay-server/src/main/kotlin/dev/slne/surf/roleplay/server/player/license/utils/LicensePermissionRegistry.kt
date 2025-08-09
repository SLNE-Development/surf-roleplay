package dev.slne.surf.roleplay.core.player.license.utils

import dev.slne.surf.surfapi.bukkit.api.permission.PermissionRegistry
import net.kyori.adventure.key.Key

object LicensePermissionRegistry : PermissionRegistry() {

    private const val PREFIX = "surf.roleplay.license"

    fun createLicensePermission(key: Key) = create(
        "${PREFIX}.license.${key.asString().replace(":", "-").replace("_", "-")}"
    )

}
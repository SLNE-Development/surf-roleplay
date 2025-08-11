package dev.slne.surf.roleplay.core.common.player.license

import net.kyori.adventure.key.Key

object LicensePermissionRegistry {
    fun createLicensePermission(key: Key)=
        "surf.roleplay.license.${key.asString().replace(":", ".").replace("_", "-")}"
}
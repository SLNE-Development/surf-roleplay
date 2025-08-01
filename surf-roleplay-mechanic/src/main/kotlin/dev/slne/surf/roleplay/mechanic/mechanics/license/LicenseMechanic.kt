package dev.slne.surf.roleplay.mechanic.mechanics.license

import dev.slne.surf.roleplay.mechanic.Mechanic
import dev.slne.surf.roleplay.mechanic.mechanics.license.player.LicensePlayerManager
import kotlinx.coroutines.Job

object LicenseMechanic : Mechanic(
    "LicenseMechanic",
    rpPlayerDisconnectHook = {
        LicensePlayerManager.remove(it.uuid)
    }
) {
    private lateinit var licenseExpirationCheck: Job

    
}
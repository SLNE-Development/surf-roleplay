package dev.slne.surf.roleplay.mechanic.mechanics.license

import dev.slne.surf.roleplay.mechanic.Mechanic
import dev.slne.surf.roleplay.mechanic.mechanics.license.player.LicensePlayerManager
import kotlinx.coroutines.Job
import kotlin.time.Duration.Companion.seconds

object LicenseMechanic : Mechanic(
    "LicenseMechanic",
    rpPlayerDisconnectHook = {
        LicensePlayerManager.remove(it.uuid)
    }
) {
    private lateinit var expirationChecker: LicenseExpirationJob
    private lateinit var job: Job

    override fun onEnable() {
        expirationChecker = LicenseExpirationJob(delay = 1.seconds)
        job = expirationChecker.start()
    }

    override fun onDisable() {
        expirationChecker.stop()
    }

}
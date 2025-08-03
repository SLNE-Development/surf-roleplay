package dev.slne.surf.roleplay.mechanic.mechanics.license

import dev.slne.surf.roleplay.api.mechanic.license.utils.LicenseRemovedReason
import dev.slne.surf.roleplay.core.utils.buildCoroutineScope
import dev.slne.surf.roleplay.mechanic.mechanics.license.player.licensePlayer
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlin.time.Duration

class LicenseExpirationJob(val delay: Duration) {

    private val supervisor: CompletableJob
    private val scope: CoroutineScope

    init {
        val (job, coroutineScope) = buildCoroutineScope("LicenseExpirationJob")

        supervisor = job
        scope = coroutineScope
    }

    fun start() = scope.launch {
        while (isActive) {
            tick()
            delay(delay)
        }
    }

    suspend fun stop() {
        supervisor.cancelAndJoin()
    }

    suspend fun tick() = newSuspendedTransaction(Dispatchers.IO) {
        val expiredLicenses = LicenseService.getAllExpiredLicenses()

        expiredLicenses.forEach { playerLicense ->
            playerLicense.player.licensePlayer()
                .removeLicense(playerLicense.license, LicenseRemovedReason.Expired)
        }
    }
}
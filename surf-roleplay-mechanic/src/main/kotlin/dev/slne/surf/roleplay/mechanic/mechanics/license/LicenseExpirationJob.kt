package dev.slne.surf.roleplay.mechanic.mechanics.license

import com.github.shynixn.mccoroutine.folia.scope
import dev.slne.surf.roleplay.api.mechanic.license.utils.LicenseRemovedReason
import dev.slne.surf.roleplay.mechanic.mechanics.license.player.licensePlayer
import dev.slne.surf.roleplay.mechanic.plugin
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlin.time.Duration

class LicenseExpirationJob(val delay: Duration) {

    private val scope =
        CoroutineScope(plugin.scope.coroutineContext + CoroutineName("LicenseExpirationJob"))

    fun start() = scope.launch {
        while (isActive) {
            tick()
            delay(delay)
        }
    }

    fun stop() {
        scope.cancel()
    }

    suspend fun tick() = newSuspendedTransaction(Dispatchers.IO) {
        val expiredLicenses = LicenseService.getAllExpiredLicenses()

        expiredLicenses.forEach { playerLicense ->
            playerLicense.player.licensePlayer()
                .removeLicense(playerLicense.license, LicenseRemovedReason.Expired)
        }
    }
}
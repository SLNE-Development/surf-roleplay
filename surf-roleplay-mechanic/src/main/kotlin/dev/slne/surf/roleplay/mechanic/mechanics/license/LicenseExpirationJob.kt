package dev.slne.surf.roleplay.mechanic.mechanics.license

import kotlinx.coroutines.*
import kotlin.time.Duration

class LicenseExpirationJob(
    val delay: Duration
) {

    private val scope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO + CoroutineName("LicenseExpirationJob"))

    fun start() = scope.launch {
        while (isActive) {
            tick()
            delay(delay)
        }
    }

    fun stop() {
        scope.cancel()
    }

    fun tick() {
        
    }

}
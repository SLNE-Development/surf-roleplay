package dev.slne.surf.roleplay.mechanic.mechanics.license

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.folia.entityDispatcher
import com.github.shynixn.mccoroutine.folia.scope
import dev.slne.surf.surfapi.core.api.messages.adventure.playSound
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import kotlinx.coroutines.*
import org.bukkit.Sound
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlin.time.Duration

class LicenseExpirationJob(
    val delay: Duration,
    val plugin: SuspendingJavaPlugin
) {

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

    suspend fun tick() =
        newSuspendedTransaction(Dispatchers.IO) {
            val expiredLicenses = LicenseService.removeAllExpiredLicenses()

            expiredLicenses.forEach { playerLicense ->
                val bukkitPlayer = playerLicense.player.bukkitPlayer ?: return@forEach

                withContext(plugin.entityDispatcher(bukkitPlayer)) {
                    bukkitPlayer.sendText {
                        appendPrefix()

                        info("Deine ")
                        append(playerLicense.license.displayName)
                        info(" Lizenz ist abgelaufen.")
                    }

                    bukkitPlayer.playSound(true) {
                        type(Sound.ENTITY_VILLAGER_HURT)
                        volume(.75f)
                        source(net.kyori.adventure.sound.Sound.Source.NEUTRAL)
                    }
                }
            }
        }
}
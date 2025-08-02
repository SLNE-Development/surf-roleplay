package dev.slne.surf.roleplay.mechanic.mechanics.license.player

import com.github.shynixn.mccoroutine.folia.globalRegionDispatcher
import dev.slne.surf.roleplay.api.mechanic.license.License
import dev.slne.surf.roleplay.api.mechanic.license.PlayerLicense
import dev.slne.surf.roleplay.api.mechanic.license.event.PlayerLicenseAddedEvent
import dev.slne.surf.roleplay.api.mechanic.license.event.PlayerLicenseRemovedEvent
import dev.slne.surf.roleplay.api.mechanic.license.player.LicensePlayer
import dev.slne.surf.roleplay.api.mechanic.license.utils.LicenseRemovedReason
import dev.slne.surf.roleplay.api.mechanic.license.utils.UnobtainableReason
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.mechanic.mechanics.license.LicenseService
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.bukkit.api.extensions.server
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import kotlinx.coroutines.withContext

class LicensePlayerImpl(val rpPlayer: RpPlayer) : LicensePlayer {

    private val _licenses = mutableObjectSetOf<PlayerLicense>()
    override val licenses get() = _licenses.freeze()

    override suspend fun addLicense(license: License): Pair<Boolean, UnobtainableReason?> {
        val (canObtain, reason) = license.canObtain(rpPlayer)

        if (!canObtain) {
            return false to reason
        }

        val event = PlayerLicenseAddedEvent(
            player = rpPlayer,
            license = license,
        )

        withContext(plugin.globalRegionDispatcher) {
            server.pluginManager.callEvent(event)
        }

        if (event.isCancelled) {
            return false to UnobtainableReason.EventCancelled(event.cancelReason)
        }

        _licenses.add(LicenseService.createPlayerLicense(license, rpPlayer))

        return true to null
    }

    override suspend fun removeLicense(license: License, reason: LicenseRemovedReason): Boolean {
        val event = PlayerLicenseRemovedEvent(
            player = rpPlayer,
            license = license,
            reason = reason
        )

        withContext(plugin.globalRegionDispatcher) {
            server.pluginManager.callEvent(event)
        }
        
        if (event.isCancelled) {
            return false
        }

        val result = LicenseService.removePlayerLicense(rpPlayer, license)

        if (result) {
            _licenses.removeIf { it.license == license }
        }

        return result
    }

    override fun getLicense(license: Class<out License>) =
        _licenses.firstOrNull { it.license::class.java == license }

    override fun hasLicense(license: Class<out License>) =
        _licenses.any { it.license::class.java == license }
}

suspend fun RpPlayer.licensePlayer() = LicensePlayerManager[uuid]
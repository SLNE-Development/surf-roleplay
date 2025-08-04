package dev.slne.surf.roleplay.mechanic.mechanics.license.player

import dev.slne.surf.roleplay.api.mechanic.license.License
import dev.slne.surf.roleplay.api.mechanic.license.PlayerLicense
import dev.slne.surf.roleplay.api.mechanic.license.event.PlayerLicenseAddedEvent
import dev.slne.surf.roleplay.api.mechanic.license.event.PlayerLicenseRemovedEvent
import dev.slne.surf.roleplay.api.mechanic.license.player.LicensePlayer
import dev.slne.surf.roleplay.api.mechanic.license.utils.LicenseCreateResult
import dev.slne.surf.roleplay.api.mechanic.license.utils.LicenseRemovedReason
import dev.slne.surf.roleplay.api.mechanic.license.utils.UnobtainableReason
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.mechanic.mechanics.license.LicenseService
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import it.unimi.dsi.fastutil.objects.ObjectSet
import java.time.ZonedDateTime

class LicensePlayerImpl(
    override val rpPlayer: RpPlayer,
) : LicensePlayer {
    private val _licenses: ObjectSet<PlayerLicense> = mutableObjectSetOf<PlayerLicense>()
    override val licenses get() = _licenses.freeze()

    fun addLicensesInternal(licenses: ObjectSet<PlayerLicense>) = _licenses.addAll(licenses)

    override suspend fun addLicense(license: License): LicenseCreateResult {
        val (canObtain, reason) = license.canObtain(this)

        if (!canObtain) {
            return LicenseCreateResult(false, reason, null)
        }

        val playerLicense = PlayerLicense(
            player = rpPlayer,
            license = license,
            expiresAt = license.expiresIn?.let {
                ZonedDateTime.now().plusSeconds(it.inWholeSeconds)
            },
        )

        val event = PlayerLicenseAddedEvent(
            player = rpPlayer,
            license = playerLicense,
        )

        if (!event.callEvent()) {
            return LicenseCreateResult(
                false,
                objectSetOf(UnobtainableReason.EventCancelled(event.cancelReason)),
                null
            )
        }

        rpPlayer.removeCashBalance(license.price)

        val createdPlayerLicense = LicenseService.createPlayerLicense(playerLicense)
        _licenses.add(createdPlayerLicense)

        return LicenseCreateResult(
            true,
            objectSetOf(),
            createdPlayerLicense
        )
    }

    override suspend fun removeLicense(license: License, reason: LicenseRemovedReason): Boolean {
        val playerLicense = getLicense(license.javaClass) ?: return false

        val event = PlayerLicenseRemovedEvent(
            player = rpPlayer,
            license = playerLicense,
            reason = reason
        )

        if (!event.callEvent()) {
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

suspend fun RpPlayer.licensePlayer() = LicensePlayerService[uuid]
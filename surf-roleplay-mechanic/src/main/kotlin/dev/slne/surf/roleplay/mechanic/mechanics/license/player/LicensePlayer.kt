package dev.slne.surf.roleplay.mechanic.mechanics.license.player

import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.mechanic.mechanics.license.License
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import java.time.ZonedDateTime

class LicensePlayer(val rpPlayer: RpPlayer) {

    private val _licenses = mutableObjectSetOf<License.PlayerLicense>()
    val licenses get() = _licenses.freeze()

    fun addLicense(license: License) {
        _licenses.add(
            License.PlayerLicense(
                player = rpPlayer,
                license = license,
                expiresAt = license.expiresIn?.let {
                    ZonedDateTime.now().plusSeconds(it.inWholeSeconds)
                }
            )
        )
    }

    fun hasLicense(license: License) = _licenses.any { it.license == license }

}

val RpPlayer.licensePlayer get() = LicensePlayerManager[uuid]
package dev.slne.surf.roleplay.api.common.player.license

import kotlin.time.Duration

interface ExpirableLicense : License {

    /**
     * The duration for which the license is valid.
     * If null, the license does not expire.
     */
    val expiresIn: Duration

}
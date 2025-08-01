package dev.slne.surf.roleplay.mechanic.mechanics.license.player

import dev.slne.surf.roleplay.api.player.RpPlayerManager
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import java.util.*

object LicensePlayerManager {

    private val _players = mutableObjectSetOf<LicensePlayer>()
    val players get() = _players.freeze()

    fun remove(uuid: UUID) = _players.removeIf { it.rpPlayer.uuid == uuid }

    operator fun get(uuid: UUID) = _players.firstOrNull { it.rpPlayer.uuid == uuid }
        ?: LicensePlayer(RpPlayerManager[uuid]).also { _players.add(it) }

}
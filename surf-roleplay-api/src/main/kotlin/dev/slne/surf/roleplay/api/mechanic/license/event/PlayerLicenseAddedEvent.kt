package dev.slne.surf.roleplay.api.mechanic.license.event

import dev.slne.surf.roleplay.api.mechanic.license.License
import dev.slne.surf.roleplay.api.player.RpPlayer
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class PlayerLicenseAddedEvent(
    val player: RpPlayer,
    val license: License,
    async: Boolean = true
) : Event(async) {

    override fun getHandlers() = handlerList

    companion object {
        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList() = handlerList
    }
}
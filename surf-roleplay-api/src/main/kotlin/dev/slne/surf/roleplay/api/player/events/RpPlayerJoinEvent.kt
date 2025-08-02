package dev.slne.surf.roleplay.api.player.events

import dev.slne.surf.roleplay.api.player.RpPlayer
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class RpPlayerJoinEvent(
    val rpPlayer: RpPlayer,
    async: Boolean = false
) : Event(async) {

    override fun getHandlers() = handlerList

    companion object {
        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList() = handlerList
    }
}
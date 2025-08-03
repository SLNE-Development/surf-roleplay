package dev.slne.surf.roleplay.api.player.events

import dev.slne.surf.roleplay.api.player.RpPlayer
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class RpPlayerQuitEvent(
    val rpPlayer: RpPlayer,
    async: Boolean = !Bukkit.isPrimaryThread()
) : Event(async) {

    override fun getHandlers() = handlerList

    companion object {
        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList() = handlerList
    }
}
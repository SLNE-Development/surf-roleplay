package dev.slne.surf.roleplay.api.events

import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

open class RpEvent(
    async: Boolean = !Bukkit.isPrimaryThread()
) : Event(async) {

    override fun getHandlers() = handlerList

    companion object {
        private val handlerList = HandlerList()
        
        @JvmStatic
        fun getHandlerList(): HandlerList = handlerList
    }
}
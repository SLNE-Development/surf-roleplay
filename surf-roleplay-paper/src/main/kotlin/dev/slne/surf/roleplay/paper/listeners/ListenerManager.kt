package dev.slne.surf.roleplay.paper.listeners

import dev.slne.surf.surfapi.bukkit.api.event.register

object ListenerManager {

    fun registerListeners() {
        OnlineListener.register()
    }

}
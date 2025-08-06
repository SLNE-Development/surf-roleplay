package dev.slne.surf.roleplay.paper.listeners

import dev.slne.surf.roleplay.paper.identity.listener.IdentityOnlineListener
import dev.slne.surf.surfapi.bukkit.api.event.register

object ListenerManager {

    fun registerListeners() {
        OnlineListener.register()
        IdentityOnlineListener.register()
    }

}
package dev.slne.surf.job.paper.listener

import dev.slne.surf.surfapi.bukkit.api.event.register

object ListenerManager {

    fun register() {
        OnlineListener.register()
    }

}
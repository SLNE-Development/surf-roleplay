package dev.slne.surf.roleplay.paper.identity.listener

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.player.events.RpPlayerJoinEvent
import dev.slne.surf.roleplay.paper.identity.dialogs.createIdentitySelectorDialog
import dev.slne.surf.roleplay.paper.plugin
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

object IdentityOnlineListener : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onRpPlayerJoin(event: RpPlayerJoinEvent) {
        plugin.launch {
            val player = event.rpPlayer
            val identities = player.identities

            if (identities.isEmpty()) {
                return@launch
            }

            if (identities.size == 1) {
                val identity = identities.first()
                player.setActiveIdentity(identity)

                return@launch
            }

            player.bukkitPlayer?.showDialog(createIdentitySelectorDialog(player))
        }
    }
}
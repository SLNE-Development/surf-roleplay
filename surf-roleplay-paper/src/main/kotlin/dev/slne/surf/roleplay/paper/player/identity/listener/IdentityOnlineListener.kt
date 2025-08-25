package dev.slne.surf.roleplay.paper.player.identity.listener

import dev.slne.surf.cloud.api.common.event.CloudEventHandler
import dev.slne.surf.roleplay.paper.player.events.RpPlayerJoinEvent
import dev.slne.surf.roleplay.paper.player.identity.dialogs.createIdentitySelectorDialog
import org.springframework.stereotype.Component

@Suppress("unused")
@Component
class IdentityOnlineListener {

    @CloudEventHandler
    fun onRpPlayerJoin(event: RpPlayerJoinEvent) {
        val player = event.player
        val identities = player.identities

        if (identities.isEmpty()) return

        if (identities.size == 1) {
            val identity = identities.first()
            player.setActiveIdentity(identity)

            return
        }

        player.player?.showDialog(createIdentitySelectorDialog(player))
    }
}
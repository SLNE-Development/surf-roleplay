package dev.slne.surf.roleplay.paper.player.license.listeners

import dev.slne.surf.cloud.api.common.event.CloudEventHandler
import dev.slne.surf.roleplay.paper.player.license.events.RpPlayerLicenseAddedEvent
import dev.slne.surf.roleplay.paper.player.license.events.RpPlayerLicenseRemovedEvent
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.playSound
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import net.kyori.adventure.sound.Sound.Source
import org.bukkit.Sound
import org.springframework.stereotype.Component

@Suppress("unused")
@Component
class LicenseChangedHandler {

    @CloudEventHandler
    fun onPlayerLicenseAdded(event: RpPlayerLicenseAddedEvent) {
        val license = event.license
        val cloudPlayer = event.player.cloudPlayer.player ?: return

        cloudPlayer.sendText {
            appendPrefix()

            info("Du hast eine ")
            append(license.license.displayName)
            info(" Lizenz erhalten.")
        }

        cloudPlayer.playSound(true) {
            type(Sound.ENTITY_PLAYER_LEVELUP)
            volume(.75f)
            source(Source.NEUTRAL)
        }
    }

    @CloudEventHandler
    fun onPlayerLicenseRemoved(event: RpPlayerLicenseRemovedEvent) {
        val license = event.license
        val cloudPlayer = event.player.cloudPlayer.player ?: return

        val reason = buildText {
            appendPrefix()
            append(event.reason.message(license))
        }

        cloudPlayer.sendMessage(reason)
        cloudPlayer.playSound(true) {
            type(Sound.ENTITY_VILLAGER_HURT)
            volume(.75f)
            source(Source.NEUTRAL)
        }
    }
}
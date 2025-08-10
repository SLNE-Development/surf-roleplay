package dev.slne.surf.roleplay.paper.player.license.listeners

import com.github.shynixn.mccoroutine.folia.entityDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.paper.player.license.events.RpPlayerLicenseAddedEvent
import dev.slne.surf.roleplay.api.paper.player.license.events.RpPlayerLicenseRemovedEvent
import dev.slne.surf.roleplay.paper.plugin
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.playSound
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object LicenseChangedHandler : Listener {

    @EventHandler
    fun onPlayerLicenseAdded(event: RpPlayerLicenseAddedEvent) {
        val license = event.license
        val cloudPlayer = event.player.cloudPlayer.player ?: return

        plugin.launch(plugin.entityDispatcher(cloudPlayer)) {
            cloudPlayer.sendText {
                appendPrefix()

                info("Du hast eine ")
                append(license.license.displayName)
                info(" Lizenz erhalten.")
            }

            cloudPlayer.playSound(true) {
                type(Sound.ENTITY_PLAYER_LEVELUP)
                volume(.75f)
                source(net.kyori.adventure.sound.Sound.Source.NEUTRAL)
            }
        }
    }

    @EventHandler
    fun onPlayerLicenseRemoved(event: RpPlayerLicenseRemovedEvent) {
        val license = event.license
        val cloudPlayer = event.player.cloudPlayer.player ?: return

        val reason = buildText {
            appendPrefix()

            event.reason.apply { message(license) }
        }

        plugin.launch(plugin.entityDispatcher(cloudPlayer)) {
            cloudPlayer.sendMessage(reason)

            cloudPlayer.playSound(true) {
                type(Sound.ENTITY_VILLAGER_HURT)
                volume(.75f)
                source(net.kyori.adventure.sound.Sound.Source.NEUTRAL)
            }
        }
    }

}
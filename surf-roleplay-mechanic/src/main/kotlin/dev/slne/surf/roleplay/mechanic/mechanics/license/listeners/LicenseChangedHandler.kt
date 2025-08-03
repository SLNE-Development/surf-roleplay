package dev.slne.surf.roleplay.mechanic.mechanics.license.listeners

import com.github.shynixn.mccoroutine.folia.entityDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.mechanic.license.event.PlayerLicenseAddedEvent
import dev.slne.surf.roleplay.api.mechanic.license.event.PlayerLicenseRemovedEvent
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.core.api.messages.adventure.playSound
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object LicenseChangedHandler : Listener {

    @EventHandler
    fun onPlayerLicenseAdded(event: PlayerLicenseAddedEvent) {
        val license = event.license
        val bukkitPlayer = event.player.bukkitPlayer ?: return

        plugin.launch(plugin.entityDispatcher(bukkitPlayer)) {
            bukkitPlayer.sendText {
                appendPrefix()

                info("Du hast eine ")
                append(license.displayName)
                info(" Lizenz erhalten.")
            }

            bukkitPlayer.playSound(true) {
                type(Sound.ENTITY_PLAYER_LEVELUP)
                volume(.75f)
                source(net.kyori.adventure.sound.Sound.Source.NEUTRAL)
            }
        }
    }

    @EventHandler
    fun onPlayerLicenseRemoved(event: PlayerLicenseRemovedEvent) {
        val license = event.license
        val bukkitPlayer = event.player.bukkitPlayer ?: return
        val reason = SurfComponentBuilder.builder().apply(event.reason.message(license))

        plugin.launch(plugin.entityDispatcher(bukkitPlayer)) {
            bukkitPlayer.sendText {
                appendPrefix()

                info("Deine ")
                append(license.displayName)
                info(" Lizenz ist abgelaufen.")
            }

            bukkitPlayer.playSound(true) {
                type(Sound.ENTITY_VILLAGER_HURT)
                volume(.75f)
                source(net.kyori.adventure.sound.Sound.Source.NEUTRAL)
            }
        }
    }

}
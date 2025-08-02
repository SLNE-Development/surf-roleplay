package dev.slne.surf.roleplay.mechanic.mechanics.test

import dev.slne.surf.roleplay.mechanic.Mechanic
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object TestMechanic : Mechanic(
    name = "TestMechanic",
    handlers = objectSetOf(
        object : Listener {
            @EventHandler
            fun onPlayerJoin(event: PlayerJoinEvent) {
                event.player.sendText {
                    primary("Willkommen zu Surf Roleplay!")
                }
            }
        }
    )
)
package dev.slne.surf.roleplay.paper.command

import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.surf.roleplay.paper.player.rpPlayer
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

fun activeIdentityCommand() = commandAPICommand("active-identity") {
    playerExecutor { player, args ->
        val rpPlayer = player.rpPlayer
        val activeIdentity = rpPlayer.activeIdentity

        player.sendText {
            appendPrefix()

            info("Deine aktive Identität: ")
            if (activeIdentity != null) {
                append(activeIdentity.type)
            } else {
                variableValue("Keine aktive Identität gesetzt")
            }
        }
    }
}
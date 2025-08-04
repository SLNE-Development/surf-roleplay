package dev.slne.surf.roleplay.mechanic.mechanics.idcard

import com.github.shynixn.mccoroutine.folia.launch
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.surf.roleplay.api.mechanic.idcard.IdCardMechanic
import dev.slne.surf.roleplay.api.player.rpPlayer
import dev.slne.surf.roleplay.mechanic.MechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanics.idcard.listeners.IdCardHandler
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object IdCardMechanicImpl : MechanicImpl(
    name = "IdCardMechanic",
    handlers = objectSetOf(
        IdCardHandler
    )
), IdCardMechanic {

    const val NPC_NAME = "ID-Card_npc"

    override suspend fun onEnable() {
        IdCardNpc.spawnNpc()

        commandAPICommand("idcard") {
            subcommand("reset-self") {
                playerExecutor { player, args ->
                    plugin.launch {
                        val rpPlayer = player.rpPlayer()

                        rpPlayer.updateInformation {
                            firstName = null
                            lastName = null
                            birthDate = null
                        }

                        player.sendText {
                            appendPrefix()

                            success("Deine ID-Karte wurde zurückgesetzt. Bitte gehe zu einem NPC, um eine neue ID-Karte zu beantragen.")
                        }
                    }
                }
            }
        }
    }
}
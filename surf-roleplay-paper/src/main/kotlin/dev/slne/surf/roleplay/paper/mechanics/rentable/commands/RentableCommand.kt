package dev.slne.surf.roleplay.paper.mechanics.rentable.commands

import com.github.shynixn.mccoroutine.folia.launch
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.kotlindsl.*
import dev.slne.surf.roleplay.api.common.mechanic.rentable.utils.events.OwnerChangeReason
import dev.slne.surf.roleplay.api.common.mechanic.rentable.utils.events.OwnerSetResult
import dev.slne.surf.roleplay.api.paper.player.rpPlayer
import dev.slne.surf.roleplay.core.common.mechanics.rentable.RentableMechanicImpl.getByKey
import dev.slne.surf.roleplay.core.common.mechanics.rentable.RentableMechanicImpl.rentables
import dev.slne.surf.roleplay.paper.plugin
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import org.bukkit.NamespacedKey

fun rentableCommand() = commandAPICommand("rentable") {
    subcommand("rent") {
        namespacedKeyArgument("rentableKey") {
            replaceSuggestions(ArgumentSuggestions.stringCollection { info ->
                rentables.map { it.key.asString() }
            })
        }

        playerExecutor { player, args ->
            val rentableKey: NamespacedKey by args
            val rentable = getByKey(rentableKey) ?: run {
                player.sendText {
                    appendPrefix()

                    error("Es konnte kein Mietobjekt mit dem Key ")
                    variableValue(rentableKey.asString())
                    error(" gefunden werden.")

                }

                return@playerExecutor
            }

            if (rentable.isRented) {
                player.sendText {
                    appendPrefix()

                    error("Das Mietobjekt ")
                    variableValue(rentableKey.asString())
                    error(" ist bereits vermietet.")

                    return@playerExecutor
                }
            }

            plugin.launch {
                val rpPlayer = player.rpPlayer

                val result = rentable.setOwner(
                    rpPlayer,
                    OwnerChangeReason.RentableBought
                )

                if (result is OwnerSetResult.Success) {
                    player.sendText {
                        appendPrefix()

                        success("Du hast das Mietobjekt ")
                        variableValue(rentableKey.asString())
                        success(" erfolgreich gemietet.")
                    }
                } else if (result is OwnerSetResult.Failure) {
                    player.sendText {
                        appendPrefix()

                        error("Das Mietobjekt ")
                        variableValue(rentableKey.asString())
                        error(" konnte nicht gemietet werden: ")
                        appendNewline(2)

                        result.reason.message(this)
                    }
                }
            }
        }
    }
}
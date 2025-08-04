@file:Suppress("UnstableApiUsage")
@file:OptIn(NmsUseWithCaution::class)

package dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.pay

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.rpPlayer
import dev.slne.surf.roleplay.api.player.utils.BalanceType
import dev.slne.surf.roleplay.core.utils.formatNumber
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.createAtmMainMenuDialog
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.feedback.createNoPlayersError
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.bukkit.api.extensions.server
import dev.slne.surf.surfapi.bukkit.api.nms.NmsUseWithCaution
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import io.papermc.paper.dialog.Dialog
import org.bukkit.entity.Player


suspend fun createSelectPlayersDialog(bukkitPlayer: Player, player: RpPlayer): Dialog {
    val balance = player.getBalance(BalanceType.BANK)
    val dialogList = buildPlayerDialogList(bukkitPlayer, player)

    if (dialogList.isEmpty()) return createNoPlayersError(bukkitPlayer, player)

    return dialog {
        base {
            title {
                primary("Geldautomat v1.0 ")
                spacer("- Überweisungen")
            }

            body {
                plainMessage(400) {
                    info("Hier kannst du Geld an andere Bürger überweisen.")
                    appendNewline(1)
                    info("Wähle, den Bürger, an welchen du Geld überweisen möchtest.")
                    appendNewline(1)
                    plainMessage(400) {
                        info("Dein aktueller Kontostand beträgt: ")
                        variableValue(bukkitPlayer.formatNumber(balance))
                        variableKey(" €€€")
                        info(".")
                        appendNewline(2)
                    }

                }
            }
            type {
                dialogList {
                    addAll(dialogList)
                    buttonWidth(200)
                    columns(1)
                    exitAction(exitPlayerSelectionButton(bukkitPlayer, player))
                }

            }
        }
    }
}

private fun exitPlayerSelectionButton(bukkitPlayer: Player, player: RpPlayer) = actionButton {
    label { text("Zurück") }
    tooltip { info("Klicke, um zum Hauptmenü zurückzukehren.") }

    action {
        playerCallback {
            plugin.launch {
                it.showDialog(createAtmMainMenuDialog(bukkitPlayer, player))
            }
        }
    }
}

private suspend fun buildPlayerDialogList(bukkitPlayer: Player, player: RpPlayer) =
    server.onlinePlayers.map { it.rpPlayer() }.filterNot { it == player }.map { onlinePlayer ->
        createAmountDialog(bukkitPlayer, player, onlinePlayer)
    }.toObjectSet()

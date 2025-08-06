@file:Suppress("UnstableApiUsage")
@file:OptIn(NmsUseWithCaution::class)

package dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.pay

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.rpPlayer
import dev.slne.surf.roleplay.api.player.utils.BalanceType
import dev.slne.surf.roleplay.api.utils.formatMoneyComponent
import dev.slne.surf.roleplay.mechanic.mechanics.atm.AtmMechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.createAtmMainMenuDialog
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.feedback.createNoPlayersError
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.bukkit.api.extensions.server
import dev.slne.surf.surfapi.bukkit.api.nms.NmsUseWithCaution
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.DialogBase


suspend fun createSelectPlayersDialog(player: RpPlayer): Dialog {
    val balance = player.getBalance(BalanceType.BANK)
    val dialogList = buildPlayerDialogList(player)

    if (dialogList.isEmpty()) return createNoPlayersError(player)

    return dialog {
        base {
            title {
                primary("Geldautomat ${AtmMechanicImpl.VERSION} ")
                spacer("— Überweisungen")
            }
            afterAction(DialogBase.DialogAfterAction.WAIT_FOR_RESPONSE)
            body {
                plainMessage(400) {
                    info("Hier kannst du Geld an andere Bürger überweisen.")
                    appendNewline()
                    info("Wähle, den Bürger, an welchen du Geld überweisen möchtest.")
                    appendNewline()
                    info("Dein aktueller Kontostand beträgt: ")
                    append { balance.formatMoneyComponent() }
                    info(".")
                    appendNewline()
                }
            }
            type {
                dialogList {
                    addAll(dialogList)
                    buttonWidth(200)
                    columns(1)
                    exitAction(exitPlayerSelectionButton(player))
                }
            }
        }
    }
}

private fun exitPlayerSelectionButton(player: RpPlayer) = actionButton {
    label { text("Zurück") }
    tooltip { info("Klicke, um zum Hauptmenü zurückzukehren.") }

    action {
        playerCallback {
            plugin.launch {
                it.showDialog(createAtmMainMenuDialog(player))
            }
        }
    }
}

private suspend fun buildPlayerDialogList(player: RpPlayer) =
    server.onlinePlayers.map { it.rpPlayer() }.filterNot { it == player }.map { onlinePlayer ->
        createAmountDialog(player, onlinePlayer)
    }.toObjectSet()

@file:Suppress("UnstableApiUsage")
@file:OptIn(NmsUseWithCaution::class)

package dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.utils.BalanceType
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.cash.createDepositDialog
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.cash.createWithdrawDialog
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.pay.createSelectPlayerPayMenu
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.clearDialogs
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.bukkit.api.nms.NmsUseWithCaution
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.ActionButton
import io.papermc.paper.registry.data.dialog.DialogBase


suspend fun createAtmMainMenuDialog(player: RpPlayer): Dialog {
    val balance = player.getBalance(BalanceType.BANK)

    return dialog {
        base {
            title { primary("Geldautomat v1.0") }
            afterAction(DialogBase.DialogAfterAction.NONE)

            body {
                plainMessage(400) {
                    info("Willkommen im Geldautomaten! Hier kannst du dein Geld verwalten.")
                    appendNewline(2)
                }
                plainMessage(400) {
                    info("Dein aktueller Kontostand beträgt: ")
                    variableValue(balance)
                    variableKey(" €€€")
                    info(".")
                    appendNewline(2)
                }
            }

        }
        type {
            multiAction {
                columns(1)
                action(payMoneyButton())
                action(depositInMoneyButton())
                action(depositOutMoneyButton())

                exitAction(exitAtmButton())
            }
        }
    }
}

private fun payMoneyButton(): ActionButton = actionButton {
    label { text("Geld überweisen") }
    tooltip { info("Klicke, um anderen Bürgern Geld zu überweisen.") }
    width(200)

    action {
        playerCallback { player ->
            plugin.launch {
                val rpPlayer = RpPlayer[player.uniqueId]

                player.showDialog(createSelectPlayerPayMenu(rpPlayer))
            }
        }
    }
}

private fun exitAtmButton(): ActionButton = actionButton {
    label { text("Geldautomat verlassen") }
    tooltip { info("Klicke, um den Geldautomaten zu verlassen.") }
    width(200)

    action {
        playerCallback { player ->
            player.clearDialogs(true)
        }
    }
}

private fun depositInMoneyButton(): ActionButton = actionButton {
    label { text("Geld einzahlen") }
    tooltip { info("Klicke, um Geld in den Geldautomaten einzuzahlen.") }
    width(200)

    action {
        playerCallback { player ->
            plugin.launch {
                val rpPlayer = RpPlayer[player.uniqueId]

                player.showDialog(createDepositDialog(rpPlayer))
            }
        }
    }
}

private fun depositOutMoneyButton(): ActionButton = actionButton {
    label { text("Geld auszahlen") }
    tooltip { info("Klicke, um Geld aus dem geldautomaten auszuzahlen.") }
    width(200)

    action {
        playerCallback { player ->
            plugin.launch {
                val rpPlayer = RpPlayer[player.uniqueId]

                player.showDialog(createWithdrawDialog(rpPlayer))
            }

        }
    }
}
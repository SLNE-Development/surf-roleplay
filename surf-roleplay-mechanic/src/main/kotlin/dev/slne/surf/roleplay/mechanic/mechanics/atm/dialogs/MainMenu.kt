@file:Suppress("UnstableApiUsage")
@file:OptIn(NmsUseWithCaution::class)

package dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.utils.BalanceType
import dev.slne.surf.roleplay.core.utils.formatNumber
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.cash.createDepositDialog
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.cash.createWithdrawDialog
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.feedback.createErrorNoBankDialog
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.feedback.createErrorNoCashDialog
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.pay.createSelectPlayersDialog
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
import org.bukkit.entity.Player


suspend fun createAtmMainMenuDialog(bukkitPlayer: Player, player: RpPlayer): Dialog {
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
                    info("Dein aktueller Kontostand beträgt ")
                    variableValue(bukkitPlayer.formatNumber(balance))
                    variableKey(" €€€")
                    info(".")
                    appendNewline(2)
                }
            }

        }
        type {
            multiAction {
                columns(1)
                action(payMoneyButton(bukkitPlayer))
                action(depositInMoneyButton(bukkitPlayer))
                action(depositOutMoneyButton(bukkitPlayer))

                exitAction(exitAtmButton())
            }
        }
    }
}

private fun payMoneyButton(bukkitPlayer: Player): ActionButton = actionButton {
    label { text("Geld überweisen") }
    tooltip { info("Klicke, um anderen Bürgern Geld zu überweisen.") }
    width(200)

    action {
        playerCallback { player ->
            plugin.launch {
                val rpPlayer = RpPlayer[player.uniqueId]

                if (!checkEnoughBank(rpPlayer)) {
                    player.showDialog(createErrorNoBankDialog(bukkitPlayer, rpPlayer))
                    return@launch
                }

                player.showDialog(createSelectPlayersDialog(bukkitPlayer, rpPlayer))
            }
        }
    }
}

private fun depositInMoneyButton(bukkitPlayer: Player): ActionButton = actionButton {
    label { text("Geld einzahlen") }
    tooltip { info("Klicke, um Geld in den Geldautomaten einzuzahlen.") }
    width(200)

    action {
        playerCallback { player ->
            plugin.launch {
                val rpPlayer = RpPlayer[player.uniqueId]

                if (!checkEnoughCash(rpPlayer)) {
                    player.showDialog(createErrorNoCashDialog(bukkitPlayer, rpPlayer))
                    return@launch
                }

                player.showDialog(createDepositDialog(bukkitPlayer, rpPlayer))
            }
        }
    }
}

private fun depositOutMoneyButton(bukkitPlayer: Player): ActionButton = actionButton {
    label { text("Geld auszahlen") }
    tooltip { info("Klicke, um Geld aus dem Geldautomaten auszuzahlen.") }
    width(200)

    action {
        playerCallback { player ->
            plugin.launch {
                val rpPlayer = RpPlayer[player.uniqueId]

                if (!checkEnoughBank(rpPlayer)) {
                    player.showDialog(createErrorNoBankDialog(bukkitPlayer, rpPlayer))
                    return@launch
                }
                player.showDialog(createWithdrawDialog(bukkitPlayer, rpPlayer))
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

private suspend fun checkEnoughCash(player: RpPlayer): Boolean {
    val balanceCash = player.getBalance(BalanceType.CASH)
    if (balanceCash >= 1) {
        return true
    }
    return false
}

private suspend fun checkEnoughBank(player: RpPlayer): Boolean {
    val balanceBank = player.getBalance(BalanceType.BANK)
    if (balanceBank >= 1) {
        return true
    }
    return false
}
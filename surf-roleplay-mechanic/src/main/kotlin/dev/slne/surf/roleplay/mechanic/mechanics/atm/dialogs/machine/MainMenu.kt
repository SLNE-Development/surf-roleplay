@file:Suppress("UnstableApiUsage")
@file:OptIn(NmsUseWithCaution::class)

package dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.machine

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.utils.BalanceType
import dev.slne.surf.roleplay.api.utils.formatMoneyComponent
import dev.slne.surf.roleplay.mechanic.mechanics.atm.AtmMechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.machine.cash.createDepositDialog
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.machine.cash.createWithdrawDialog
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.machine.feedback.createErrorNoBankDialog
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.machine.feedback.createErrorNoCashDialog
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.machine.pay.createSelectPlayersDialog
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
            title { primary("Geldautomat ${AtmMechanicImpl.ATM_VERSION}") }
            afterAction(DialogBase.DialogAfterAction.WAIT_FOR_RESPONSE)

            body {
                plainMessage(400) {
                    info("Willkommen im Geldautomaten!")
                    appendNewline()
                    info("Hier kannst du dein Geld verwalten.")
                    appendNewline(2)
                    info("Dein aktueller Kontostand beträgt ")
                    append(balance.formatMoneyComponent())
                    info(".")
                    appendNewline()
                }
            }
        }
        type {
            multiAction {
                columns(1)
                action(payMoneyButton(player))
                action(depositInMoneyButton(player))
                action(depositOutMoneyButton(player))

                exitAction(exitAtmButton())
            }
        }
    }
}

private fun payMoneyButton(rpPlayer: RpPlayer): ActionButton = actionButton {
    label { text("Geld überweisen") }
    tooltip { info("Klicke, um anderen Bürgern Geld zu überweisen.") }
    width(200)

    action {
        playerCallback { player ->
            plugin.launch {
                if (!checkEnoughBank(rpPlayer)) {
                    player.showDialog(createErrorNoBankDialog(rpPlayer))
                    return@launch
                }
                player.showDialog(createSelectPlayersDialog(rpPlayer))
            }
        }
    }
}

private fun depositInMoneyButton(rpPlayer: RpPlayer): ActionButton = actionButton {
    label { text("Geld einzahlen") }
    tooltip { info("Klicke, um Geld in den Geldautomaten einzuzahlen.") }
    width(200)

    action {
        playerCallback { player ->
            plugin.launch {

                if (!checkEnoughCash(rpPlayer)) {
                    player.showDialog(createErrorNoCashDialog(rpPlayer))
                    return@launch
                }

                player.showDialog(createDepositDialog(rpPlayer))
            }
        }
    }
}

private fun depositOutMoneyButton(rpPlayer: RpPlayer): ActionButton = actionButton {
    label { text("Geld auszahlen") }
    tooltip { info("Klicke, um Geld aus dem Geldautomaten auszuzahlen.") }
    width(200)

    action {
        playerCallback { player ->
            plugin.launch {

                if (!checkEnoughBank(rpPlayer)) {
                    player.showDialog(createErrorNoBankDialog(rpPlayer))
                    return@launch
                }
                player.showDialog(createWithdrawDialog(rpPlayer))
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
    return balanceCash >= 1
}

private suspend fun checkEnoughBank(player: RpPlayer): Boolean {
    val balanceBank = player.getBalance(BalanceType.BANK)
    return balanceBank >= 1
}
@file:Suppress("UnstableApiUsage")
@file:OptIn(NmsUseWithCaution::class)

package dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs

import com.github.shynixn.mccoroutine.folia.launch
import com.jeff_media.morepersistentdatatypes.DataType
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.utils.BalanceType
import dev.slne.surf.roleplay.api.utils.formatMoneyComponent
import dev.slne.surf.roleplay.mechanic.mechanics.atm.AtmMechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.bankaccount.createBankAccountDialog
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.cash.createDepositDialog
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.cash.createWithdrawDialog
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.feedback.*
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
import org.bukkit.NamespacedKey

private val bankCardKey = NamespacedKey("surf-roleplay-mechanic", "bank_card")
private val idCardOwnerKey = NamespacedKey("surf-roleplay-mechanic", "id_card_owner")
private val hasVerified = true
private val pinRegex by lazy { Regex("^\\d{4}\$") }

suspend fun createAtmMainMenuDialog(player: RpPlayer, hasCard: Boolean): Dialog {
    val balance = player.getBalance(BalanceType.BANK)

    return dialog {
        if (!hasCard) {
            base {
                title { primary("Geldautomat ${AtmMechanicImpl.ATM_VERSION}") }
                afterAction(DialogBase.DialogAfterAction.WAIT_FOR_RESPONSE)

                body {
                    plainMessage(400) {
                        error("Es konnte keine Bankkarte gefunden werden.")
                        appendNewline()
                        error("Bitte versuche es erneut.")
                        appendNewline()
                        info("Solltest du keine Bankkarte besitzen, er stelle dir ein Bankkonto.")
                        appendNewline()
                    }
                }
            }
            type {
                multiAction {
                    columns(1)
                    action(createBankAccountButton(player))

                    exitAction(exitAtmButton())
                }
            }
        } else {
            if (!hasVerified) {

                base {
                    title { primary("Geldautomat ${AtmMechanicImpl.ATM_VERSION}") }
                    afterAction(DialogBase.DialogAfterAction.WAIT_FOR_RESPONSE)

                    body {
                        plainMessage(400) {
                            info("Willkommen im Geldautomaten!")
                            appendNewline()
                            info("Hier kannst du dein Geld verwalten.")
                            appendNewline(2)
                            info("Tippe deinen PIN ein, um fortzufahren")
                            appendNewline()
                        }
                        input {
                            text("pin") {
                                label { text("PIN eingeben:") }
                                maxLength(4)
                                width(200)
                            }
                        }
                    }
                }
                type {
                    confirmation(confirmEnteredPin(player), exitAtmButton())
                }

            } else {
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

private fun createBankAccountButton(rpPlayer: RpPlayer): ActionButton = actionButton {
    label { text("Bankkonto erstellen") }
    tooltip { info("Klicke, um ein Bankkonto zu erstellen") }
    width(200)

    action {
        playerCallback { player ->
            plugin.launch {
                val hasIdCard = player.inventory.contents.any { item ->
                    item?.let {
                        val owner = it.persistentDataContainer.get(idCardOwnerKey, DataType.UUID)
                        val bankCard = it.persistentDataContainer.get(bankCardKey, DataType.BYTE)

                        owner == player.uniqueId && bankCard != null
                    } == true
                }

                if (!hasIdCard) {
                    player.showDialog(createNoIdCardFoundError(rpPlayer))
                    return@launch
                }

                player.showDialog(createBankAccountDialog(rpPlayer))
            }
        }
    }
}

private fun confirmEnteredPin(player: RpPlayer): ActionButton = actionButton {
    label { text("Bestätigen") }
    tooltip { info("Klicke, um dich mit dem angegebenen PIN einzuloggen.") }
    width(200)

    action {
        customClick { info, audience ->
            val enteredPin = info.getText("pin") ?: ""

            if (!enteredPin.matches(pinRegex)) {
                audience.showDialog(createInvalidEnteredPinError(player))
                return@customClick
            }

            val pin = enteredPin.toInt()

            plugin.launch {
                //search in database for pin
                val playerPin = 1234
                if (pin != playerPin) {
                    audience.showDialog(createPinInvalidError(player))
                    return@launch
                }
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
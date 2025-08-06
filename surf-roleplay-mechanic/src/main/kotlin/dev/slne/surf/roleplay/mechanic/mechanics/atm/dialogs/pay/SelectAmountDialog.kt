@file:Suppress("UnstableApiUsage")
@file:OptIn(NmsUseWithCaution::class)

package dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.pay

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.mechanic.atm.event.PlayerTransferBankMoneyEvent
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.utils.BalanceType
import dev.slne.surf.roleplay.api.utils.formatMoneyComponent
import dev.slne.surf.roleplay.mechanic.mechanics.atm.AtmMechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.feedback.createGenericErrorDialog
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.feedback.createInvalidAmountEnteredPayError
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.feedback.createInvalidAmountPayError
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.feedback.createSuccessPayDialog
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.bukkit.api.nms.NmsUseWithCaution
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.ActionButton

private val validAmountInput = Regex("^\\d+$")

suspend fun createAmountDialog(player: RpPlayer, selectedPlayer: RpPlayer): Dialog {
    val balance = player.getBalance(BalanceType.BANK)

    return dialog {
        base {
            title {
                primary("Geldautomat ${AtmMechanicImpl.VERSION} ")
                spacer("- Einzahlung")
            }
            externalTitle {
                spacer(selectedPlayer.username.toString())
            }
            body {
                plainMessage(400) {
                    info("Dein aktueller Kontostand beträgt: ")
                    append(balance.formatMoneyComponent())
                    variableKey(" €€€")
                    info(".")
                    appendNewline(2)
                }
                plainMessage(400) {
                    info("Wähle, den Betrag, welchen du an den Bürger ")
                    append(selectedPlayer)
                    info(" überweisen möchtest.")
                    appendNewline(2)
                }
                input {
                    text("pay_amount") {
                        label { text("Bitte Betrag wählen:") }
                        width(200)
                    }
                }
            }
            type {
                confirmation(
                    confirmPayButton(player, selectedPlayer),
                    exitPayButton(player)
                )
            }
        }
    }
}

private fun confirmPayButton(player: RpPlayer, selectedPlayer: RpPlayer): ActionButton =
    actionButton {
        label { text("Geld überweisen") }
        tooltip {
            info("Klicke hier, um mit der Überweisung fortzufahren.")
        }

        action {
            customPlayerClick { info, audience ->
                val text = info.getText("pay_amount") ?: "0"
                if (!isValidAmount(text)) {
                    audience.showDialog(createInvalidAmountEnteredPayError(player, selectedPlayer))
                    return@customPlayerClick
                }
                plugin.launch {
                    val amount = text.toInt()

                    if (amount <= 0 || amount > player.getBalance(BalanceType.BANK)) {
                        audience.showDialog(createInvalidAmountPayError(player, selectedPlayer))
                        return@launch
                    }
                    ////////////////////////////////////
                    var state = false

                    val event = PlayerTransferBankMoneyEvent(
                        player = player,
                        receiver = selectedPlayer,
                        amount = amount
                    )

                    if (event.callEvent()) {
                        state = player.transferBankBalance(selectedPlayer, amount)
                    }
                    ////////////////////////////////////

                    if (!state) {
                        audience.showDialog(createGenericErrorDialog(player))
                        return@launch
                    }
                    audience.showDialog(createSuccessPayDialog(player, amount, selectedPlayer))
                }
            }
        }
    }


fun exitPayButton(player: RpPlayer): ActionButton = actionButton {
    label { text("Zurück") }
    tooltip {
        info("Klicke hier, um zurück zur Bürger-Auswahl zu gelangen.")
    }
    action {
        customClick { info, audience ->
            plugin.launch {
                audience.showDialog(createSelectPlayersDialog(player))
            }
        }
    }
}

fun isValidAmount(input: String): Boolean {
    return validAmountInput.matches(input)
}

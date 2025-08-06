@file:Suppress("UnstableApiUsage")
@file:OptIn(NmsUseWithCaution::class)

package dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.cash

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.mechanic.atm.event.PlayerWithdrawMoneyEvent
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.utils.BalanceType
import dev.slne.surf.roleplay.api.utils.formatMoneyComponent
import dev.slne.surf.roleplay.mechanic.mechanics.atm.AtmMechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.createAtmMainMenuDialog
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.feedback.createCashWithdrawError
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.feedback.createCashWithdrawSuccess
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.feedback.createInvalidAmountWithdrawError
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.bukkit.api.nms.NmsUseWithCaution
import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.ActionButton
import io.papermc.paper.registry.data.dialog.DialogBase

private val validAmountInput by lazy {
    Regex("^\\d+$")
}

suspend fun createWithdrawDialog(player: RpPlayer): Dialog {
    val balance = player.getBalance(BalanceType.BANK)

    return dialog {
        base {
            title {
                primary("Geldautomat ${AtmMechanicImpl.VERSION} ")
                spacer("— Auszahlung")
            }
            afterAction(DialogBase.DialogAfterAction.WAIT_FOR_RESPONSE)
            body {
                plainMessage(400) {
                    info("Hier kannst du dir Geld auszahlen lassen.")
                    appendNewline()
                    info("Das Geld wird somit von dein Konto abgebucht.")
                    appendNewline()
                    info("Dein aktueller Kontostand beträgt: ")
                    append(balance.formatMoneyComponent())
                    info(".")
                    appendNewline()
                }
                input {
                    text("deposit_amount") {
                        label { text("Bitte Betrag wählen:") }
                        width(200)
                    }
                }
            }
        }
        type {
            confirmation(
                withdrawMoneyButton(player),
                exitWithdrawMoneyButton(player)
            )
        }
    }
}

private fun withdrawMoneyButton(player: RpPlayer): ActionButton = actionButton {
    label { text("Geld auszahlen") }
    tooltip {
        info("Klicke hier, um dir den angegebenen Geldbetrag auszahlen zu lassen.")
    }
    action {
        customClick { info, audience ->
            val text = info.getText("deposit_amount") ?: "0"

            if (!isValidAmount(text)) {
                audience.showDialog(createInvalidAmountWithdrawError(player))
                return@customClick
            }

            plugin.launch {
                val amount = text.toInt()
                val balance = player.getBalance(BalanceType.BANK)

                if (amount <= 0 || amount > balance) {
                    audience.showDialog(createInvalidAmountWithdrawError(player))
                    return@launch
                }
                val event = PlayerWithdrawMoneyEvent(
                    player = player,
                    amount = amount
                )

                if (!event.callEvent()) {
                    return@launch
                }

                val stateBank = player.removeBankBalance(amount)
                val stateCash = player.addCashBalance(amount)

                if (!stateCash || !stateBank) {
                    audience.showDialog(createCashWithdrawError(player))
                    return@launch
                }

                audience.showDialog(createCashWithdrawSuccess(player, amount))
            }
        }
    }
}

fun exitWithdrawMoneyButton(player: RpPlayer): ActionButton = actionButton {
    label { text("Abbrechen") }
    tooltip {
        info("Klicke hier, um die Auszahlung abzubrechen und zum Hauptmenü zurückzukehren.")
    }

    action {
        customClick { info, audience ->
            plugin.launch {
                audience.showDialog(createAtmMainMenuDialog(player))
            }
        }
    }
}

fun isValidAmount(input: String) = validAmountInput.matches(input)
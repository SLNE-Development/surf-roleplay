@file:Suppress("UnstableApiUsage")
@file:OptIn(NmsUseWithCaution::class)

package dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.cash

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.utils.BalanceType
import dev.slne.surf.roleplay.api.utils.formatMoney
import dev.slne.surf.roleplay.mechanic.mechanics.atm.AtmMechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.createAtmMainMenuDialog
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.feedback.createCashDepositError
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.feedback.createCashDepositSuccess
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.feedback.createInvalidAmountDepositError
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.feedback.createInvalidAmountEnteredCashError
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.bukkit.api.nms.NmsUseWithCaution
import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.ActionButton

suspend fun createDepositDialog(player: RpPlayer): Dialog {
    val balanceCash = player.getBalance(BalanceType.CASH)

    return dialog {
        base {
            title {
                primary("Geldautomat ${AtmMechanicImpl.VERSION} ")
                spacer("- Einzahlung")
            }

            body {
                plainMessage(400) {
                    info("Hier kannst du Geld einzahlen.")
                    appendNewline()
                    info("Das Geld wird somit auf dein Konto gebucht.")
                    appendNewline()
                    info("Dein derzeitiger Bargeldbestand beträgt ")
                    variableValue(balanceCash.formatMoney())
                    info(".")
                    appendNewline()
                }
            }
            input {
                text("deposit_amount") {
                    label { text("Bitte Betrag wählen:") }
                    width(200)
                }
            }
        }
        type {
            confirmation(
                depositMoneyButton(player),
                exitDepositMoneyButton(player)
            )
        }
    }
}

private fun depositMoneyButton(player: RpPlayer): ActionButton = actionButton {
    label { text("Geld einzahlen") }
    tooltip {
        info("Klicke hier, um die angegebene Geldmenge einzuzahlen")
    }
    action {
        customClick { info, audience ->
            val text = info.getText("deposit_amount") ?: "0"

            if (!isValidAmount(text)) {
                audience.showDialog(createInvalidAmountEnteredCashError(player))
                return@customClick
            }

            plugin.launch {
                val amount = text.toInt()
                val balanceCash = player.getBalance(BalanceType.CASH)

                if (amount <= 0 || amount > balanceCash) {
                    audience.showDialog(createInvalidAmountDepositError(player))
                    return@launch
                }

                val stateBank = player.removeCashBalance(amount)
                val stateCash = player.addBankBalance(amount)

                if (!stateCash || !stateBank) {
                    audience.showDialog(createCashDepositError(player))
                    return@launch
                }

                audience.showDialog(createCashDepositSuccess(player, amount))
            }
        }
    }
}

private fun exitDepositMoneyButton(player: RpPlayer): ActionButton = actionButton {
    label { text("Abbrechen") }
    tooltip {
        info("Klicke hier, um die Einzahlung abzubrechen und zum Hauptmenü zurückzukehren.")
    }
    action {
        customClick { info, audience ->
            plugin.launch {
                audience.showDialog(createAtmMainMenuDialog(player))
            }
        }
    }
}
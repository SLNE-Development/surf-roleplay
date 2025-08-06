@file:Suppress("UnstableApiUsage")
@file:OptIn(NmsUseWithCaution::class)

package dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.cash

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.utils.BalanceType
import dev.slne.surf.roleplay.api.utils.formatMoney
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.createAtmMainMenuDialog
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.feedback.createCashDepositError
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.feedback.createCashWithdrawSuccess
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.feedback.createInvalidAmountCashError
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.feedback.createInvalidAmountPayError
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.bukkit.api.nms.NmsUseWithCaution
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.ActionButton
import org.bukkit.entity.Player

suspend fun createDepositDialog(bukkitPlayer: Player, player: RpPlayer): Dialog {
    val balance = player.getBalance(BalanceType.BANK)
    val balanceCash = player.getBalance(BalanceType.CASH)

    return dialog {

        base {
            title {
                primary("Geldautomat v1.0 ")
                spacer("- Einzahlung")
            }

            body {
                plainMessage(400) {
                    info("Hier kannst du Geld in den Geldautomaten einzahlen. Das Geld wird somit auf dein Konto gebucht.")
                    appendNewline(2)
                }
                plainMessage(400) {
                    info("Du führst aktuell ")
                    variableValue(balanceCash.formatMoney())
                    variableKey(" €€€")
                    info(" mit dir.")
                    appendNewline(2)
                }
            }
            input {
                text("deposit_amount") {
                    label { text("Betrag wählen") }
                    width(200)
                }
            }
        }
        type {
            confirmation(
                depositMoneyButton(bukkitPlayer, player),
                exitDepositMoneyButton(bukkitPlayer, player)
            )
        }
    }
}

private fun depositMoneyButton(bukkitPlayer: Player, player: RpPlayer): ActionButton = actionButton {
    label { text("Geld einzahlen") }
    tooltip {
        info("Klicke hier, um die angegebene Geldmenge einzuzahlen")
    }
    action {
        customClick { info, audience ->
            plugin.launch {
                val text = info.getText("deposit_amount") ?: "0"

                if (!isValidAmount(text)) {
                    audience.showDialog(createInvalidAmountPayError(bukkitPlayer, player))
                    return@launch
                }
                val amount = text.toFloat()
                val balanceCash = player.getBalance(BalanceType.CASH)

                if (amount <= 0 || amount > balanceCash) {
                    audience.showDialog(
                        createInvalidAmountCashError(bukkitPlayer, player)
                    )
                    return@launch
                }

                val stateBank = player.removeCashBalance(amount.toDouble())
                val stateCash = player.addBankBalance(amount.toDouble())

                if (!stateCash || !stateBank) {
                    audience.showDialog(
                        createCashDepositError(bukkitPlayer, player)
                    )
                    return@launch
                }

                audience.showDialog(createCashWithdrawSuccess(bukkitPlayer, player, amount.toDouble()))

                audience.showDialog(createAtmMainMenuDialog(bukkitPlayer, player))
            }
        }
    }
}

private fun exitDepositMoneyButton(bukkitPlayer: Player, player: RpPlayer): ActionButton = actionButton {
    label { text("Abbrechen") }
    tooltip {
        info("Klicke hier, um die Einzahlung abzubrechen.")
    }
    action {
        customClick { info, audience ->
            plugin.launch {
                audience.showDialog(createAtmMainMenuDialog(bukkitPlayer, player))
            }
        }
    }
}
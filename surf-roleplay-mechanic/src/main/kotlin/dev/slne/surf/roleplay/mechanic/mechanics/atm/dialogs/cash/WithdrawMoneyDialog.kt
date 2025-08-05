@file:Suppress("UnstableApiUsage")
@file:OptIn(NmsUseWithCaution::class)

package dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.cash

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.utils.BalanceType
import dev.slne.surf.roleplay.core.utils.formatNumber
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.createAtmMainMenuDialog
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.feedback.createCashWithdrawError
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.feedback.createCashWithdrawSuccess
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.feedback.createInvalidAmountCashError
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


suspend fun createWithdrawDialog(bukkitPlayer: Player, player: RpPlayer): Dialog {
    val balance = player.getBalance(BalanceType.BANK)

    return dialog {

        base {
            title {
                primary("Geldautomat v1.0 ")
                spacer("- Auszahlung")
            }

            body {
                plainMessage(400) {
                    info("Hier kannst du dir Geld vom Geldautomaten auszahlen lassen. Das Geld wird somit von dein Konto abgebucht.")
                    appendNewline(2)
                }
                plainMessage(400) {
                    info("Dein aktueller Kontostand beträgt: ")
                    variableValue(bukkitPlayer.formatNumber(balance))
                    variableKey(" €€€")
                    info(".")
                    appendNewline(2)
                }

                input {
                    numberRange("deposit_amount", 0..balance.toInt()) {
                        label { text("Betrag wählen") }
                        step(1.0f)
                        width(800)
                    }
                }

            }
        }
        type {
            confirmation(
                withdrawMoneyButton(bukkitPlayer, player),
                exitWithdrawMoneyButton(bukkitPlayer, player)
            )
        }
    }
}

private fun withdrawMoneyButton(bukkitPlayer: Player, player: RpPlayer): ActionButton = actionButton {
    label { text("Geld auszahlen") }
    tooltip {
        info("Klicke hier, um dir den angegebenen Geldbetrag auszahlen zu lassen.")
    }
    action {
        customClick { info, audience ->

            //get value, remove money, add items
            //feedback screen error / success

            plugin.launch {
                val amount = info.getFloat("deposit_amount") ?: 0f
                val balance = player.getBalance(BalanceType.BANK)


                if (amount <= 0 || amount > balance) {
                    audience.showDialog(
                        createInvalidAmountCashError(bukkitPlayer, player)
                    )
                    return@launch
                }

                val stateBank = player.removeBankBalance(amount.toDouble())
                val stateCash = player.addCashBalance(amount.toDouble())

                if (!stateCash || !stateBank) {
                    audience.showDialog(
                        createCashWithdrawError(bukkitPlayer, player)
                    )
                    return@launch
                }

                audience.showDialog(createCashWithdrawSuccess(bukkitPlayer, player, amount.toDouble()))
            }
        }
    }
}

fun exitWithdrawMoneyButton(bukkitPlayer: Player, player: RpPlayer): ActionButton = actionButton {
    label { text("Abbrechen") }
    tooltip {
        info("Klicke hier, um die Auszahlung abzubrechen.")
    }
    action {
        customClick { info, audience ->
            plugin.launch {
                audience.showDialog(createAtmMainMenuDialog(bukkitPlayer, player))
            }
        }
    }
}
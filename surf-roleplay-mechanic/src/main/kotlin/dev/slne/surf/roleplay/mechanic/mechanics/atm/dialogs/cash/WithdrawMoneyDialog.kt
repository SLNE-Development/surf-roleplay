@file:Suppress("UnstableApiUsage")
@file:OptIn(NmsUseWithCaution::class)

package dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.cash

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.utils.BalanceType
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.createAtmMainMenuDialog
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.bukkit.api.nms.NmsUseWithCaution
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.ActionButton


suspend fun createWithdrawDialog(player: RpPlayer): Dialog {
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
                    variableValue(balance)
                    variableKey(" €€€")
                    info(".")
                    appendNewline(2)
                }
            }
            input {
                numberRange("deposit_out_amount", 1f..balance.toFloat()) {
                    label { text("Betrag wählen") }
                    step(1.0f)
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

            //get value, remove money, add items
            //feedback screen error / success

            plugin.launch {
                audience.showDialog(createAtmMainMenuDialog(player))
            }
        }
    }
}

fun exitWithdrawMoneyButton(player: RpPlayer): ActionButton = actionButton {
    label { text("Abbrechen") }
    tooltip {
        info("Klicke hier, um die Auszahlung abzubrechen.")
    }
    action {
        customClick { info, audience ->
            plugin.launch {
                audience.showDialog(createAtmMainMenuDialog(player))
            }
        }
    }
}
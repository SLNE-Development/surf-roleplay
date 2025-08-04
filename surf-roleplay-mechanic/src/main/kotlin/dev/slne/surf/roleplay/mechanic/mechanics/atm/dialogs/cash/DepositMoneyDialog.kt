@file:Suppress("UnstableApiUsage")
@file:OptIn(NmsUseWithCaution::class)

package dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.cash

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.utils.BalanceType
import dev.slne.surf.roleplay.core.utils.formatNumber
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
                    info("Dein aktueller Kontostand beträgt: ")
                    variableValue(bukkitPlayer.formatNumber(balance))
                    variableKey(" €€€")
                    info(".")
                    appendNewline(2)
                }
            }
            input {
                numberRange("deposit_in_amount", 1f..balanceCash.toFloat()) {
                    label { text("Betrag wählen") }
                    step(1.0f)
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

            //get value, book money, delete items
            //feedback screen error / success

            plugin.launch {
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
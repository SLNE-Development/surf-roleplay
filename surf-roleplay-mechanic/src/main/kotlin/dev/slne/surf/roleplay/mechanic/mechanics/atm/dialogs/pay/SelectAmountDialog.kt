@file:Suppress("UnstableApiUsage")
@file:OptIn(NmsUseWithCaution::class)

package dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.pay

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.utils.BalanceType
import dev.slne.surf.roleplay.core.utils.formatNumber
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.feedback.createGenericErrorDialog
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.feedback.createInvalidAmountError
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
import org.bukkit.entity.Player

suspend fun createAmountDialog(bukkitPlayer: Player, player: RpPlayer, selectedPlayer: RpPlayer): Dialog {
    val balance = player.getBalance(BalanceType.BANK)

    return dialog {
        base {
            title {
                primary("Geldautomat v1.0 ")
                spacer("- Einzahlung")
            }
            externalTitle {
                spacer(selectedPlayer.username.toString())
            }

            body {
                plainMessage(400) {
                    info("Dein aktueller Kontostand beträgt: ")
                    variableValue(bukkitPlayer.formatNumber(balance))
                    variableKey(" €€€")
                    info(".")
                    appendNewline(2)
                }
                plainMessage(400) {
                    info("Wähle, den Betrag, welchen du an den Bürger ")
                    variableValue(selectedPlayer.username.toString())
                    info(" überweisen möchtest.")
                    appendNewline(2)
                }
                input {
                    input {
                        numberRange("pay_amount", 0..balance.toInt()) {
                            label { text("Betrag wählen") }
                            step(1.0f)
                            width(800)
                        }
                    }
                }
            }
            type {
                confirmation(
                    confirmPayButton(bukkitPlayer, player, selectedPlayer),
                    exitPayButton(bukkitPlayer, player)
                )
            }
        }
    }
}

private fun confirmPayButton(bukkitPlayer: Player, player: RpPlayer, selectedPlayer: RpPlayer): ActionButton =
    actionButton {
        label { text("Geld überweisen") }
        tooltip {
            info("Klicke hier, um mit der Überweisung fortzufahren.")
        }
        action {
            customPlayerClick { info, audience ->
                //get value, remove money, add items
                //feedback screen error / success

                plugin.launch {
                    val amount = info.getFloat("pay_amount") ?: 0.0f

                    if (amount <= 0 || amount > player.getBalance(BalanceType.BANK)) {
                        audience.showDialog(createInvalidAmountError(bukkitPlayer, player, selectedPlayer))
                        return@launch
                    }

                    val state = player.transferBankBalance(selectedPlayer, amount.toDouble())

                    if (state) {
                        audience.showDialog(createSuccessPayDialog(audience, player, amount, selectedPlayer))
                        return@launch
                    }
                    audience.showDialog(createGenericErrorDialog(bukkitPlayer, player))
                }

            }
        }
    }


fun exitPayButton(bukkitPlayer: Player, player: RpPlayer): ActionButton = actionButton {
    label { text("Zurück") }
    tooltip {
        info("Klicke hier, um zurück zur Bürger-Auswahl zu gelangen.")
    }
    action {
        customClick { info, audience ->
            plugin.launch {
                audience.showDialog(createSelectPlayersDialog(bukkitPlayer, player))
            }
        }
    }
}
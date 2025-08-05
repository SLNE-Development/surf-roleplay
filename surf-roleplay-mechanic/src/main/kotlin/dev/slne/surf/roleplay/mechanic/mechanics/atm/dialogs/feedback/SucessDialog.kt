@file:Suppress("UnstableApiUsage")
@file:OptIn(NmsUseWithCaution::class)

package dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.feedback

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.core.utils.formatNumber
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.createAtmMainMenuDialog
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.bukkit.api.nms.NmsUseWithCaution
import io.papermc.paper.dialog.Dialog
import org.bukkit.entity.Player

fun createSuccessPayDialog(bukkitPlayer: Player, player: RpPlayer, amount: Float, receiver: RpPlayer): Dialog {

    return dialog {
        base {
            title {
                primary("Geldautomat v1.0 ")
                spacer("- Erfolgreich")
            }
            body {
                plainMessage(400) {
                    success("Du hast erfolgreich ")
                    variableValue(bukkitPlayer.formatNumber(amount))
                    variableKey(" €€€")
                    success(" an den Bürger ")
                    variableValue(receiver.username.toString())
                    success(" überwiesen.")
                }
            }
            type {
                notice(exitSuccessButton(bukkitPlayer, player))
            }
        }
    }
}

fun createCashWithdrawSuccess(bukkitPlayer: Player, player: RpPlayer, amount: Double): Dialog {

    return dialog {
        base {
            title {
                primary("Geldautomat v1.0 ")
                spacer("- Erfolgreich")
            }

            body {
                plainMessage(400) {
                    success("Dir wurden erfolgreich ")
                    variableValue(bukkitPlayer.formatNumber(amount))
                    variableKey(" €€€")
                    success(" vom Konto abgebucht und ausgezahlt.")
                }
            }
            type {
                notice(exitSuccessButton(bukkitPlayer, player))
            }
        }
    }
}

private fun exitSuccessButton(bukkitPlayer: Player, player: RpPlayer) = actionButton {
    label { text("Zurück") }
    tooltip { info("Klicke, um zum Hauptmenü zurückzukehren.") }

    action {
        playerCallback {
            plugin.launch {
                it.showDialog(createAtmMainMenuDialog(bukkitPlayer, player))
            }
        }
    }
}
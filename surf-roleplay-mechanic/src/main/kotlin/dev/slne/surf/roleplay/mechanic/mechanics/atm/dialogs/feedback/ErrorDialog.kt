@file:Suppress("UnstableApiUsage")
@file:OptIn(NmsUseWithCaution::class)

package dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.feedback

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.createAtmMainMenuDialog
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.pay.createAmountDialog
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.bukkit.api.nms.NmsUseWithCaution
import io.papermc.paper.dialog.Dialog
import org.bukkit.entity.Player

fun createErrorNoCashDialog(bukkitPlayer: Player, player: RpPlayer): Dialog {

    return dialog {
        base {
            title {
                primary("Geldautomat v1.0 ")
                spacer("- Systemfehler")
            }

            body {
                plainMessage(400) {
                    error("Du besitzt kein Bargeld.")
                }

            }
            type {
                notice(exitErrorButton(bukkitPlayer, player))
            }
        }
    }
}

fun createErrorNoBankDialog(bukkitPlayer: Player, player: RpPlayer): Dialog {

    return dialog {
        base {
            title {
                primary("Geldautomat v1.0 ")
                spacer("- Systemfehler")
            }

            body {
                plainMessage(400) {
                    error("Du besitzt kein angelegtes Geld auf deinem Konto.")
                }

            }
            type {
                notice(exitErrorButton(bukkitPlayer, player))
            }
        }
    }
}

fun createGenericErrorDialog(bukkitPlayer: Player, player: RpPlayer): Dialog {

    return dialog {
        base {
            title {
                primary("Geldautomat v1.0 ")
                spacer("- Systemfehler")
            }

            body {
                plainMessage(400) {
                    error("Es ist ein Fehler aufgetreten.")
                }
            }
            type {
                notice(exitErrorButton(bukkitPlayer, player))
            }
        }
    }
}

fun createInvalidAmountPayError(bukkitPlayer: Player, player: RpPlayer): Dialog {

    return dialog {
        base {
            title {
                primary("Geldautomat v1.0 ")
                spacer("- Systemfehler")
            }

            body {
                plainMessage(400) {
                    error("Du kannst diesen Betrag nicht überweisen.")
                }
            }
            type {
                notice(exitErrorButton(bukkitPlayer, player))
            }
        }
    }
}

fun createInvalidAmountCashError(bukkitPlayer: Player, player: RpPlayer): Dialog {

    return dialog {
        base {
            title {
                primary("Geldautomat v1.0 ")
                spacer("- Systemfehler")
            }

            body {
                plainMessage(400) {
                    error("Du kannst dir diesen Betrag nicht auszahlen lassen.")
                }
            }
            type {
                notice(exitInvalidAmountCashWithdrawButton(bukkitPlayer, player))
            }
        }
    }
}


fun createCashWithdrawError(bukkitPlayer: Player, player: RpPlayer): Dialog {

    return dialog {
        base {
            title {
                primary("Geldautomat v1.0 ")
                spacer("- Systemfehler")
            }

            body {
                plainMessage(400) {
                    error("Die Auszahlung ist fehlgeschlagen, bitte versuche es später erneut.")
                }
            }
            type {
                notice(exitInvalidAmountCashWithdrawButton(bukkitPlayer, player))
            }
        }
    }
}

fun createCashDepositError(bukkitPlayer: Player, player: RpPlayer): Dialog {

    return dialog {
        base {
            title {
                primary("Geldautomat v1.0 ")
                spacer("- Systemfehler")
            }

            body {
                plainMessage(400) {
                    error("Die Einzahlung ist fehlgeschlagen, bitte versuche es später erneut.")
                }
            }
            type {
                notice(exitInvalidAmountCashWithdrawButton(bukkitPlayer, player))
            }
        }
    }
}

fun createNoPlayersError(bukkitPlayer: Player, player: RpPlayer): Dialog {

    return dialog {
        base {
            title {
                primary("Geldautomat v1.0 ")
                spacer("- Systemfehler")
            }
            body {
                plainMessage(400) {
                    error("Es befinden sich aktuell keine Bürger in der Stadt, an welche du Geld überweisen kannst.")
                }
            }
            type {
                notice(exitErrorButton(bukkitPlayer, player))
            }
        }
    }
}

private fun exitInvalidAmountPayButton(bukkitPlayer: Player, player: RpPlayer, selectedPlayer: RpPlayer) =
    actionButton {
        label { text("Zurück") }
        tooltip { info("Klicke, um zum Hauptmenü zurückzukehren.") }

        action {
            playerCallback {
                plugin.launch {
                    it.showDialog(createAmountDialog(bukkitPlayer, player, selectedPlayer))
                }
            }
        }
    }

private fun exitInvalidAmountCashWithdrawButton(bukkitPlayer: Player, player: RpPlayer) =
    actionButton {
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

private fun exitErrorButton(bukkitPlayer: Player, player: RpPlayer) = actionButton {
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
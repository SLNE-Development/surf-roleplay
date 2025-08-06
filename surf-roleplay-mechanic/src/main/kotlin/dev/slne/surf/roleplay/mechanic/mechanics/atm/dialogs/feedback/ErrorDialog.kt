@file:Suppress("UnstableApiUsage")
@file:OptIn(NmsUseWithCaution::class)

package dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.feedback

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.mechanic.mechanics.atm.AtmMechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.cash.createDepositDialog
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.cash.createWithdrawDialog
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.createAtmMainMenuDialog
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.pay.createAmountDialog
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.bukkit.api.nms.NmsUseWithCaution
import io.papermc.paper.dialog.Dialog

fun createErrorNoCashDialog(player: RpPlayer): Dialog = dialog {
    base {
        title {
            primary("Geldautomat ${AtmMechanicImpl.VERSION} ")
            spacer("- Systemfehler")
        }

        body {
            plainMessage(400) {
                error("Du besitzt kein Bargeld.")
            }

        }
        type {
            notice(exitErrorButton(player))
        }
    }
}

fun createErrorNoBankDialog(player: RpPlayer): Dialog = dialog {
    base {
        title {
            primary("Geldautomat ${AtmMechanicImpl.VERSION} ")
            spacer("- Systemfehler")
        }
        body {
            plainMessage(400) {
                error("Du besitzt kein angelegtes Geld auf deinem Konto.")
            }
        }
        type {
            notice(exitErrorButton(player))
        }
    }
}

fun createGenericErrorDialog(player: RpPlayer): Dialog = dialog {
    base {
        title {
            primary("Geldautomat ${AtmMechanicImpl.VERSION} ")
            spacer("- Systemfehler")
        }

        body {
            plainMessage(400) {
                error("Es ist ein Fehler aufgetreten.")
            }
        }
        type {
            notice(exitErrorButton(player))
        }
    }
}

fun createTransactionErrorDialog(player: RpPlayer): Dialog = dialog {
    base {
        title {
            primary("Geldautomat ${AtmMechanicImpl.VERSION}")
            spacer("- Systemfehler")
        }

        body {
            plainMessage(400) {
                error("Bei der Überweisung ist ein Fehler aufgetreten.")
                appendNewline()
                error("Bitte versuche es später erneut.")
            }
        }
        type {
            notice(exitErrorButton(player))
        }
    }
}

fun createInvalidAmountPayError(player: RpPlayer, receiver: RpPlayer): Dialog = dialog {
    base {
        title {
            primary("Geldautomat ${AtmMechanicImpl.VERSION} ")
            spacer("- Systemfehler")
        }

        body {
            plainMessage(400) {
                error("Der eingegebene Betrag konnte nicht überwiesen werden.")
                appendNewline()
                error("Bitte versuche es erneut.")
            }
        }
        type {
            notice(exitBackToPlayerSelectionErrorButton(player, receiver))
        }
    }
}

fun createInvalidAmountEnteredPayError(player: RpPlayer, receiver: RpPlayer): Dialog = dialog {
    base {
        title {
            primary("Geldautomat ${AtmMechanicImpl.VERSION} ")
            spacer("- Systemfehler")
        }
        body {
            plainMessage(400) {
                error("Der angegebene Betrag konnte nicht ausgelesen werden.")
                appendNewline()
                error("Bitte versuche es erneut.")
            }
        }
        type {
            notice(exitBackToPlayerSelectionErrorButton(player, receiver))
        }
    }
}

fun createInvalidAmountEnteredCashError(player: RpPlayer): Dialog = dialog {
    base {
        title {
            primary("Geldautomat ${AtmMechanicImpl.VERSION} ")
            spacer("- Systemfehler")
        }
        body {
            plainMessage(400) {
                error("Der angegebene Betrag konnte nicht ausgelesen werden.")
                appendNewline()
                error("Bitte versuche es erneut.")
            }
        }
        type {
            notice(exitErrorButton(player))
        }
    }
}

fun createInvalidAmountDepositError(player: RpPlayer): Dialog = dialog {
    base {
        title {
            primary("Geldautomat ${AtmMechanicImpl.VERSION} ")
            spacer("- Systemfehler")
        }

        body {
            plainMessage(400) {
                error("Du kannst diesen Betrag nicht einzahlen.")
                appendNewline()
                error("Bitte versuche es erneut.")
            }
        }
        type {
            notice(exitBackToWithdrawAmountEnterErrorButton(player))
        }
    }
}

fun createInvalidAmountWithdrawError(player: RpPlayer): Dialog = dialog {
    base {
        title {
            primary("Geldautomat ${AtmMechanicImpl.VERSION} ")
            spacer("- Systemfehler")
        }

        body {
            plainMessage(400) {
                error("Der angegebene Betrag konnte nicht ausgelesen werden.")
                appendNewline()
                error("Bitte versuche es erneut.")
            }
        }
        type {
            notice(exitInvalidAmountDepositButton(player))
        }
    }
}


fun createCashWithdrawError(player: RpPlayer): Dialog = dialog {
    base {
        title {
            primary("Geldautomat ${AtmMechanicImpl.VERSION} ")
            spacer("- Systemfehler")
        }

        body {
            plainMessage(400) {
                error("Die Auszahlung ist fehlgeschlagen. ")
                appendNewline()
                error("Bitte versuche es später erneut.")
            }
        }
        type {
            notice(exitWithdrawErrorButton(player))
        }
    }
}

fun createCashDepositError(player: RpPlayer): Dialog = dialog {
    base {
        title {
            primary("Geldautomat ${AtmMechanicImpl.VERSION} ")
            spacer("- Systemfehler")
        }

        body {
            plainMessage(400) {
                error("Die Einzahlung ist fehlgeschlagen, bitte versuche es später erneut.")
            }
        }
        type {
            notice(exitInvalidAmountCashWithdrawButton(player))
        }
    }
}

fun createNoPlayersError(player: RpPlayer): Dialog = dialog {
    base {
        title {
            primary("Geldautomat ${AtmMechanicImpl.VERSION} ")
            spacer("- Systemfehler")
        }
        body {
            plainMessage(400) {
                error("Es befinden sich aktuell keine Bürger in der Stadt, an welche du Geld überweisen kannst.")
                appendNewline()
                error("Versuche es später erneut.")
            }
        }
        type {
            notice(exitErrorButton(player))
        }
    }
}

private fun exitInvalidAmountPayButton(player: RpPlayer, selectedPlayer: RpPlayer) =
    actionButton {
        label { text("Zurück") }
        tooltip { info("Klicke, um zum Hauptmenü zurückzukehren.") }

        action {
            playerCallback {
                plugin.launch {
                    it.showDialog(createAmountDialog(player, selectedPlayer))
                }
            }
        }
    }

private fun exitInvalidAmountCashWithdrawButton(player: RpPlayer) =
    actionButton {
        label { text("Zurück") }
        tooltip { info("Klicke, um zum Hauptmenü zurückzukehren.") }

        action {
            playerCallback {
                plugin.launch {
                    it.showDialog(createAtmMainMenuDialog(player))
                }
            }
        }
    }

private fun exitInvalidAmountDepositButton(player: RpPlayer) =
    actionButton {
        label { text("Zurück") }
        tooltip { info("Klicke, um zum Hauptmenü zurückzukehren.") }

        action {
            playerCallback {
                plugin.launch {
                    it.showDialog(createDepositDialog(player))
                }
            }
        }
    }

private fun exitInvalidAmountWithdrawButton(player: RpPlayer) =
    actionButton {
        label { text("Zurück") }
        tooltip { info("Klicke, um zum Hauptmenü zurückzukehren.") }

        action {
            playerCallback {
                plugin.launch {
                    it.showDialog(createWithdrawDialog(player))
                }
            }
        }
    }

private fun exitErrorButton(player: RpPlayer) = actionButton {
    label { text("Zurück") }
    tooltip { info("Klicke, um zum Hauptmenü zurückzukehren.") }

    action {
        playerCallback {
            plugin.launch {
                it.showDialog(createAtmMainMenuDialog(player))
            }
        }
    }
}

private fun exitBackToPlayerSelectionErrorButton(player: RpPlayer, receiver: RpPlayer) = actionButton {
    label { text("Zurück") }
    tooltip { info("Klicke, um zur Betragseingabe zurückzukehren.") }

    action {
        playerCallback {
            plugin.launch {
                it.showDialog(createAmountDialog(player, receiver))
            }
        }
    }
}

private fun exitBackToWithdrawAmountEnterErrorButton(player: RpPlayer) = actionButton {
    label { text("Zurück") }
    tooltip { info("Klicke, um zur Betragseingabe zurückzukehren.") }

    action {
        playerCallback {
            plugin.launch {
                it.showDialog(createWithdrawDialog(player))
            }
        }
    }
}

private fun exitWithdrawErrorButton(player: RpPlayer) = actionButton {
    label { text("Zurück") }
    tooltip { info("Klicke, um zur Betragseingabe zurückzukehren.") }

    action {
        playerCallback {
            plugin.launch {
                it.showDialog(createWithdrawDialog(player))
            }
        }
    }
}
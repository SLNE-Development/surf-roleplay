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

fun createInvalidAmountError(bukkitPlayer: Player, player: RpPlayer, selectedPlayer: RpPlayer): Dialog {

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
                notice(exitInvalidAmountButton(bukkitPlayer, player, selectedPlayer))
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

private fun exitInvalidAmountButton(bukkitPlayer: Player, player: RpPlayer, selectedPlayer: RpPlayer) = actionButton {
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
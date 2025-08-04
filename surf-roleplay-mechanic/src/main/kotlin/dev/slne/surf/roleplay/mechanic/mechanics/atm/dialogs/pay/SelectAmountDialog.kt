@file:Suppress("UnstableApiUsage")
@file:OptIn(NmsUseWithCaution::class)

package dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.pay

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.createAtmMainMenuDialog
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.bukkit.api.nms.NmsUseWithCaution
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline

private const val BALANCE_AMOUNT = 1000.0f

fun createMainTransferMenu(player: RpPlayer) = dialog {

    base {
        title {
            primary("Geldautomat v1.0 ")
            spacer("- Überweisungen")
        }

        body {
            plainMessage(400) {
                info("Hier kannst du Geld an andere Bürger überweisen.")
                appendNewline(2)
                plainMessage(400) {
                    info("Dein aktueller Kontostand beträgt: ")
                    variableValue(BALANCE_AMOUNT)
                    variableKey(" €€€")
                    info(".")
                    appendNewline(2)
                }
                plainMessage(400) {
                    info("Wähle, den Bürger, an welchen du Geld überweisen möchtest.")
                    appendNewline(2)
                }
            }
            input {
                //dialoglist with onlineplayers

                //button action: open amount selection dialog
            }
        }
        type {
            notice {
                label { text("Abbrechen") }
                tooltip {
                    info("Klicke, um die Überweisung abzubrechen.")
                }

                action {
                    customClick { info, audience ->

                        plugin.launch {
                            audience.showDialog(createAtmMainMenuDialog(player))
                        }
                    }
                }
            }
        }
    }
}


//    private fun payMoneyButton(): ActionButton = actionButton {
//        label { text("Geld überweisen") }
//        tooltip { info("Klicke, um die Überweisung abzuschließen.") }
//        width(200)
//
//        action {
//            playerCallback { player ->
//
//                // send money
//                // show confirmation dialog
//                player.showDialog(createAtmMainMenuDialog())
//            }
//        }
//    }
//
//    private fun cancelMoneyButton(): ActionButton = actionButton {
//        label { text("Abbrechen") }
//        tooltip { info("Klicke, um die Überweisung abzubrechen.") }
//        width(200)
//
//        action {
//            playerCallback { player ->
//                player.showDialog(createAtmMainMenuDialog())
//            }
//        }
//    }
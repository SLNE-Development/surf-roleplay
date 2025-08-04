@file:Suppress("UnstableApiUsage")
@file:OptIn(NmsUseWithCaution::class)

package dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.pay

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.utils.BalanceType
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.createAtmMainMenuDialog
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.bukkit.api.nms.NmsUseWithCaution
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import io.papermc.paper.dialog.Dialog


suspend fun createSelectPlayerPayMenu(player: RpPlayer): Dialog {
    val balance = player.getBalance(BalanceType.BANK)

    return dialog {
        base {
            title {
                primary("Geldautomat v1.0 ")
                spacer("- Überweisungen")
            }

            body {
                plainMessage(400) {
                    info("Hier kannst du Geld an andere Bürger überweisen.")
                    appendNewline(1)
                    info("Wähle, den Bürger, an welchen du Geld überweisen möchtest.")
                    appendNewline(1)
                    plainMessage(400) {
                        info("Dein aktueller Kontostand beträgt: ")
                        variableValue(balance)
                        variableKey(" €€€")
                        info(".")
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
                        playerCallback { player ->
                            plugin.launch {
                                val rpPlayer = RpPlayer[player.uniqueId]

                                player.showDialog(createAtmMainMenuDialog(rpPlayer))
                            }
                        }
                    }
                }
            }

        }
    }
}
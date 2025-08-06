@file:Suppress("UnstableApiUsage")
@file:OptIn(NmsUseWithCaution::class)

package dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.feedback

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.utils.formatMoneyComponent
import dev.slne.surf.roleplay.mechanic.mechanics.atm.AtmMechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.createAtmMainMenuDialog
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.bukkit.api.nms.NmsUseWithCaution
import io.papermc.paper.dialog.Dialog

fun createSuccessPayDialog(player: RpPlayer, amount: Int, receiver: RpPlayer): Dialog = dialog {
    base {
        title {
            primary("Geldautomat ${AtmMechanicImpl.VERSION} ")
            spacer("- Erfolgreich")
        }
        body {
            plainMessage(400) {
                success("Du hast erfolgreich ")
                append(amount.formatMoneyComponent())
                success(" an den Bürger ")
                append(receiver)
                success(" überwiesen.")
            }
        }
        type {
            notice(exitSuccessButton(player))
        }
    }
}

fun createCashWithdrawSuccess(player: RpPlayer, amount: Int): Dialog = dialog {
    base {
        title {
            primary("Geldautomat ${AtmMechanicImpl.VERSION} ")
            spacer("- Erfolgreich")
        }

        body {
            plainMessage(400) {
                success("Dir wurden erfolgreich ")
                append(amount.formatMoneyComponent())
                success(" vom Konto abgebucht und ausgezahlt.")
            }
        }
        type {
            notice(exitSuccessButton(player))
        }
    }
}

fun createCashDepositSuccess(player: RpPlayer, amount: Int): Dialog = dialog {
    base {
        title {
            primary("Geldautomat ${AtmMechanicImpl.VERSION} ")
            spacer("- Erfolgreich")
        }

        body {
            plainMessage(400) {
                success("Du hast erfolgreich ")
                append(amount.formatMoneyComponent())
                success(" auf dein Konto aufgebucht.")
            }
        }
        type {
            notice(exitSuccessButton(player))
        }
    }
}

private fun exitSuccessButton(player: RpPlayer) = actionButton {
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
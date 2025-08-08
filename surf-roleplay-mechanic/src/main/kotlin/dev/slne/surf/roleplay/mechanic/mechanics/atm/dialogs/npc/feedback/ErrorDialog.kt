@file:Suppress("UnstableApiUsage")
@file:OptIn(NmsUseWithCaution::class)

package dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.npc.feedback

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.mechanic.mechanics.atm.AtmMechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.machine.createAtmMainMenuDialog
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.npc.createNpcMainMenuDialog
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.bukkit.api.nms.NmsUseWithCaution
import io.papermc.paper.dialog.Dialog

fun createInvalidPinError(player: RpPlayer): Dialog = dialog {
    base {
        title {
            primary("Bankberater ${AtmMechanicImpl.NPC_VERSION} ")
        }
//        afterAction(DialogBase.DialogAfterAction.WAIT_FOR_RESPONSE)
        body {
            plainMessage(400) {
                error("Dein eingegebener PIN ist ungültig")
                appendNewline()
                error("Dein PIN muss aus ")
                variableValue("vier Ziffern ")
                error("bestehen.")
                appendNewline()
                error("Bitte versuche es erneut.")
            }
        }
        type {
            notice(exitInvalidPinButton(player))
        }
    }
}

fun createBankAccountCreationError(player: RpPlayer): Dialog = dialog {
    base {
        title {
            primary("Bankberater ${AtmMechanicImpl.NPC_VERSION} ")
        }
//        afterAction(DialogBase.DialogAfterAction.WAIT_FOR_RESPONSE)
        body {
            plainMessage(400) {
                error("Es tut mir leid.")
                appendNewline()
                error("Die Systeme sind aktuell überlastet.")
                appendNewline()
                error("Erinnere mich später nochmal daran.")
            }
        }
        type {
            notice(exitGeneralErrorButton(player))
        }
    }
}

private fun exitInvalidPinButton(player: RpPlayer) = actionButton {
    label { text("Zurück") }
    tooltip { info("Klicke, um zur Kontoerstellung zurückzukehren.") }

    action {
        playerCallback {
            plugin.launch {
                it.showDialog(createAtmMainMenuDialog(player))
            }
        }
    }
}

private fun exitGeneralErrorButton(player: RpPlayer) = actionButton {
    label { text("Zurück") }
    tooltip { info("Klicke, um zur Übersicht zurückzukehren.") }

    action {
        playerCallback {
            plugin.launch {
                it.showDialog(createNpcMainMenuDialog(player))
            }
        }
    }
}


@file:Suppress("UnstableApiUsage")
@file:OptIn(NmsUseWithCaution::class)

package dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.npc.feedback

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.mechanic.mechanics.atm.AtmMechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.npc.createNpcMainMenuDialog
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.bukkit.api.nms.NmsUseWithCaution
import io.papermc.paper.dialog.Dialog

fun createBankAccountCreationSuccess(player: RpPlayer, pin: Int): Dialog = dialog {
    base {
        title {
            primary("Bankberater ${AtmMechanicImpl.NPC_VERSION} ")
        }
//        afterAction(DialogBase.DialogAfterAction.WAIT_FOR_RESPONSE)
        body {
            plainMessage(400) {
                success("Du hast erfolgreich ein Konto mit dem PIN ")
                variableValue(pin)
                appendNewline()
                success(" erstellt.")
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
                it.showDialog(createNpcMainMenuDialog(player))
            }
        }
    }
}
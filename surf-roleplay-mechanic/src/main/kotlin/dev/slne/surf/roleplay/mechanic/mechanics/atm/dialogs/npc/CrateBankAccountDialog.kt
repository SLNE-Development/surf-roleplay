@file:Suppress("UnstableApiUsage")
@file:OptIn(NmsUseWithCaution::class)

package dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.npc

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.mechanic.mechanics.atm.AtmMechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.machine.createAtmMainMenuDialog
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.npc.feedback.createBankAccountCreationSuccess
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.npc.feedback.createInvalidPinError
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.bukkit.api.nms.NmsUseWithCaution
import io.papermc.paper.dialog.Dialog


private val validPinInput by lazy {
    Regex("^[0-9]{4}\$\"")
}


fun createBankAccountDialog(player: RpPlayer): Dialog = dialog {
    base {
        title {
            primary("Bankberater ${AtmMechanicImpl.NPC_VERSION} ")
            spacer("— Konto erstellen")
        }
//            afterAction(DialogBase.DialogAfterAction.WAIT_FOR_RESPONSE)
        body {
            plainMessage(400) {
                info("Du bist dabei dir ein Konto anzulegen.")
                appendNewline()
            }
        }

        input {
            text("pin") {
                label { text("PIN") }
                width(200)
                maxLength(4)
            }
        }
        type {
            confirmation(
                confirmBankAccountCreationButton(player),
                exitBankAccountCreationButton(player)
            )
        }
    }
}


private fun confirmBankAccountCreationButton(player: RpPlayer) = actionButton {
    label { text("Konto erstellen") }
    tooltip { info("Klicke, um dein Konto zu erstellen.") }
    width(200)

    action {
        playerCallback {
            customClick { info, audience ->
                val text = info.getText("pin") ?: ""

                if (!validPinInput.matches(text)) {
                    audience.showDialog(createInvalidPinError(player))
                    return@customClick
                }

                val pin = text.toInt()

                plugin.launch {

//                    val status = player.createBankAccount(pin)

//                    if (status) {
//                        audience.showDialog(createInvalidPinError(player))
//                        return@launch
//                    }

                    it.inventory.addItem(BankCard.bankCard(player))
                    audience.showDialog(createBankAccountCreationSuccess(player, pin))
                }
            }
        }
    }
}

private fun exitBankAccountCreationButton(player: RpPlayer) = actionButton {
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
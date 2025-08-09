@file:Suppress("UnstableApiUsage") @file:OptIn(NmsUseWithCaution::class)

package dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.bankaccount

import com.github.shynixn.mccoroutine.folia.entityDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.mechanic.mechanics.atm.AtmMechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanics.atm.BankCard
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.createAtmMainMenuDialog
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.feedback.createBankAccountCreationSuccess
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.feedback.createPinNotEqualError
import dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.feedback.createPinNotMatchingRegexError
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.bukkit.api.nms.NmsUseWithCaution
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.ActionButton
import io.papermc.paper.registry.data.dialog.DialogBase
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.*

private val pinRegex by lazy { Regex("^\\d{4}\$") }


fun createBankAccountDialog(player: RpPlayer): Dialog = dialog {
    base {
        title { primary("Geldautomat ${AtmMechanicImpl.ATM_VERSION}") }
        afterAction(DialogBase.DialogAfterAction.WAIT_FOR_RESPONSE)

        body {
            plainMessage(400) {
                info("Du bist dabei ein Bankkonto zu erstellen.")
                appendNewline(2)
                info("Bitte lege einen ")
                variableValue("vierstelligen Zahlencode ")
                info("fest.")
                appendNewline()
                info("Dieser Zahlencode wird später benötigt, um auf dein Bankkonto zuzugreifen.")
            }
            input {
                text("pin") {
                    label { text("PIN auswählen:") }
                    width(200)
                    maxLength(4)
                }
                text("pin_repeat") {
                    label { text("PIN wiederholen:") }
                    width(200)
                    maxLength(4)
                }
            }
        }
    }
    type {
        confirmation(confirmAccountCreationButton(player), exitAccountCreationButton(player))
    }
}

private fun confirmAccountCreationButton(player: RpPlayer) = actionButton {
    label { text("Bankkonto erstellen") }
    tooltip { info("Klicke, um dein Bankkonto zu erstellen.") }

    action {
        customPlayerClick { info, audience ->

            val pin = info.getText("pin") ?: "1"
            val pinRepeat = info.getText("pin_repeat") ?: "2"

            if (!pin.matches(pinRegex) || !pinRepeat.matches(pinRegex)) {
                audience.showDialog(createPinNotMatchingRegexError(player))
                return@customPlayerClick
            }
            if (pin != pinRepeat) {
                audience.showDialog(createPinNotEqualError(player))
                return@customPlayerClick
            }

            plugin.launch {
                val now = LocalDate.now()
                val cardId = UUID.randomUUID()
                //create bank account in db

                withContext(plugin.entityDispatcher(audience)) {
                    audience.inventory.addItem(BankCard.bankCard(player, now, cardId))
                    audience.showDialog(createBankAccountCreationSuccess(player, pin.toInt()))
                }
            }
        }
    }
}

private fun exitAccountCreationButton(player: RpPlayer): ActionButton = actionButton {
    label { text("Zurück") }
    tooltip { info("Klicke, um zum Hauptmenü zurückzukehren.") }

    action {
        playerCallback {
            plugin.launch {
                it.showDialog(createAtmMainMenuDialog(player, false))
            }
        }
    }
}


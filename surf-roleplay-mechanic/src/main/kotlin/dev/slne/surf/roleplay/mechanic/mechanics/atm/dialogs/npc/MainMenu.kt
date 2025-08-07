@file:Suppress("UnstableApiUsage")
@file:OptIn(NmsUseWithCaution::class)

package dev.slne.surf.roleplay.mechanic.mechanics.atm.dialogs.npc

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.mechanic.mechanics.atm.AtmMechanicImpl
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.clearDialogs
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.bukkit.api.nms.NmsUseWithCaution
import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.ActionButton
import io.papermc.paper.registry.data.dialog.DialogBase

fun createNpcMainMenuDialog(player: RpPlayer): Dialog {

    return dialog {
        base {
            title { primary("Geldautomat ${AtmMechanicImpl.VERSION}") }
            afterAction(DialogBase.DialogAfterAction.WAIT_FOR_RESPONSE)

            body {
                plainMessage(400) {
                    info("Herzlich willkommen!")
                    appendNewline()
                    info("Ich bin Robert, dein persönlicher Bankberater")
                    appendNewline()
                    info("Wie kann ich Ihnen weiterhelfen?")
                }
            }
        }
        type {
            multiAction {
                columns(1)
                action(createBankAccount(player))
                action(changeBankAccountPin(player))
                action(resetBankAccountPin(player))
                action(deleteBankAccount(player))

                exitAction(exitNpButton())
            }
        }
    }
}

private fun createBankAccount(rpPlayer: RpPlayer): ActionButton = actionButton {
    label { text("Konto erstellen") }
    tooltip { info("Klicke, um dir ein Konto anzulegen.") }
    width(200)

    action {
        playerCallback { player ->
            plugin.launch {
//                player.showDialog(createSelectPlayersDialog(rpPlayer))
            }
        }
    }
}

private fun changeBankAccountPin(rpPlayer: RpPlayer): ActionButton = actionButton {
    label { text("Pin ändern") }
    tooltip { info("Klicke, um Geld in den Geldautomaten einzuzahlen.") }
    width(200)

    action {
        playerCallback { player ->
            plugin.launch {
//                player.showDialog(createDepositDialog(rpPlayer))
            }
        }
    }
}

private fun createBanCardLost(rpPlayer: RpPlayer): ActionButton = actionButton {
    label { text("Bankkarte verloren") }
    tooltip { info("Klicke, um dir ein Konto anzulegen.") }
    width(200)

    action {
        playerCallback { player ->
            plugin.launch {
//                player.showDialog(createSelectPlayersDialog(rpPlayer))
            }
        }
    }
}

private fun resetBankAccountPin(rpPlayer: RpPlayer): ActionButton = actionButton {
    label { text("Pin zurücksetzen") }
    tooltip { info("Klicke, um Geld aus dem Geldautomaten auszuzahlen.") }
    width(200)

    action {
        playerCallback { player ->
            plugin.launch {
//                player.showDialog(createWithdrawDialog(rpPlayer))
            }
        }
    }
}

private fun deleteBankAccount(rpPlayer: RpPlayer): ActionButton = actionButton {
    label { text("Konto löschen") }
    tooltip { info("Klicke, um Geld aus dem Geldautomaten auszuzahlen.") }
    width(200)

    action {
        playerCallback { player ->
            plugin.launch {
//                player.showDialog(createWithdrawDialog(rpPlayer))
            }
        }
    }
}

private fun exitNpButton(): ActionButton = actionButton {
    label { text("Tschüss") }
    tooltip { info("Klicke, um das Gespräch mit Robert zu beenden.") }
    width(200)

    action {
        playerCallback { player ->
            player.clearDialogs(true)
        }
    }
}
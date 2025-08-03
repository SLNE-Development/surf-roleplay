@file:Suppress("UnstableApiUsage")

package dev.slne.surf.roleplay.mechanic.mechanics.license.dialogs

import dev.slne.surf.roleplay.api.mechanic.license.player.LicensePlayer
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import io.papermc.paper.registry.data.dialog.DialogBase

fun myLicensesDialog(licensePlayer: LicensePlayer) = dialog {
    val dialogList = buildDialogList(licensePlayer)

    base {
        title { primary("Meine Lizenzen") }
        afterAction(DialogBase.DialogAfterAction.NONE)

        body {
            plainMessage(400) {
                info("Hier findest du eine Übersicht über alle deine Lizenzen.")
                appendNewline(2)
                info("Du kannst auf eine Lizenz klicken, um weitere Details zu sehen.")

                if (dialogList.isEmpty()) {
                    appendNewline(2)
                    info("Du hast derzeit keine Lizenzen.")
                }
            }
        }
    }

    type {
        dialogList {
            addAll(dialogList)
            buttonWidth(300)
            columns(1)
            exitAction(backButton(licensePlayer))
        }
    }
}

private fun backButton(licensePlayer: LicensePlayer) = actionButton {
    label { text("Zurück") }
    tooltip { info("Klicke, um zum Hauptmenü zurückzukehren.") }

    action {
        playerCallback { player ->
            player.showDialog(licenseDialog(licensePlayer))
        }
    }
}

private fun buildDialogList(licensePlayer: LicensePlayer) = licensePlayer.licenses.map { license ->
    myLicenseDialog(licensePlayer, license)
}.toObjectSet()
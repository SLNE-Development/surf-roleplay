@file:Suppress("UnstableApiUsage")

package dev.slne.surf.roleplay.core.player.license.dialogs

import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.identity.RpIdentity
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import io.papermc.paper.registry.data.dialog.DialogBase

fun myLicensesDialog(player: RpPlayer, identity: RpIdentity) = dialog {
    val dialogList = buildDialogList(player, identity)

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
            exitAction(backButton(player, identity))
        }
    }
}

private fun backButton(player: RpPlayer, identity: RpIdentity) = actionButton {
    label { text("Zurück") }
    tooltip { info("Klicke, um zum Hauptmenü zurückzukehren.") }

    action {
        playerCallback {
            it.showDialog(licenseDialog(player, identity))
        }
    }
}

private fun buildDialogList(
    player: RpPlayer, identity: RpIdentity
) = player.licenses.map { license ->
    myLicenseDialog(player, identity, license)
}.toObjectSet()
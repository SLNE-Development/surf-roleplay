@file:Suppress("UnstableApiUsage")

package dev.slne.surf.roleplay.mechanic.mechanics.license.dialogs

import dev.slne.surf.roleplay.api.mechanic.Mechanic
import dev.slne.surf.roleplay.api.mechanic.license.LicenseMechanic
import dev.slne.surf.roleplay.api.mechanic.license.player.LicensePlayer
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import io.papermc.paper.registry.data.dialog.DialogBase

fun buyLicensesDialog(licensePlayer: LicensePlayer) = dialog {
    base {
        title { primary("Lizenzen erwerben") }
        afterAction(DialogBase.DialogAfterAction.NONE)

        body {
            plainMessage(400) {
                info("Hier kannst du Lizenzen erwerben.")
            }
            plainMessage(400) {}
            plainMessage(400) {
                info("Wähle eine Lizenz aus, um sie zu kaufen.")
            }
            plainMessage(400) {}
            plainMessage(400) {
                info("Einige Lizenzen können Voraussetzungen haben, die erfüllt sein müssen, bevor du sie erwerben kannst.")
            }
            plainMessage(400) {
                info("Du findest die Voraussetzungen in der Beschreibung der Lizenz.")
            }
        }
    }

    type {
        dialogList {
            addAll(buildDialogList(licensePlayer))
            columns(1)
            buttonWidth(300)
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

private fun buildDialogList(
    licensePlayer: LicensePlayer
) = Mechanic.getMechanic<LicenseMechanic>().licenses.filterNot {
    licensePlayer.hasLicense(it)
}.map {
    buyLicenseDialog(licensePlayer, it)
}
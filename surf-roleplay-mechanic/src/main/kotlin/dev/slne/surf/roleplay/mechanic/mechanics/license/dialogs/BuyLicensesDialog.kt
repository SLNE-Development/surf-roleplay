@file:Suppress("UnstableApiUsage")

package dev.slne.surf.roleplay.mechanic.mechanics.license.dialogs

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.mechanic.Mechanic
import dev.slne.surf.roleplay.api.mechanic.license.LicenseMechanic
import dev.slne.surf.roleplay.api.mechanic.license.player.LicensePlayer
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import io.papermc.paper.registry.data.dialog.DialogBase

fun buyLicensesDialog(licensePlayer: LicensePlayer) = dialog {
    base {
        title { primary("Lizenzen erwerben") }
        afterAction(DialogBase.DialogAfterAction.WAIT_FOR_RESPONSE)

        body {
            plainMessage(400) {
                info("Hier kannst du Lizenzen erwerben.")
                appendSpace()
                info("Wähle eine Lizenz aus, um sie zu kaufen.")
                appendSpace()
                info("Einige Lizenzen können Voraussetzungen haben, die erfüllt sein müssen, bevor du sie erwerben kannst.")

                appendNewline(2)
                info("Du findest die Voraussetzungen in der Beschreibung der Lizenz.")
            }
        }
    }

    type {
        multiAction {
            buildDialogList(licensePlayer).forEach { action(it) }
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

private fun buildDialogList(
    licensePlayer: LicensePlayer
) = Mechanic.getMechanic<LicenseMechanic>().licenses.filterNot {
    licensePlayer.hasLicense(it)
}.map { license ->
    actionButton {
        label(license.displayName)
        tooltip { info("Klicke, um zur Detailansicht zu gelangen.") }
        width(300)

        action {
            playerCallback {
                plugin.launch {
                    it.showDialog(buyLicenseDialog(licensePlayer, license))
                }
            }
        }
    }
}
@file:Suppress("UnstableApiUsage")

package dev.slne.surf.roleplay.mechanic.mechanics.license.dialogs

import dev.slne.surf.roleplay.api.mechanic.license.PlayerLicense
import dev.slne.surf.roleplay.api.mechanic.license.player.LicensePlayer
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.DialogBase

fun myLicenseDialog(licensePlayer: LicensePlayer, playerLicense: PlayerLicense): Dialog = dialog {
    val license = playerLicense.license

    base {
        title(playerLicense.license.displayName)
        externalTitle { append(playerLicense.license.displayName) }
        afterAction(DialogBase.DialogAfterAction.NONE)

        body {
            plainMessage(400) {
                info("Hier findest du Details zu deiner Lizenz.")
                appendNewline(2)

                variableKey("Lizenz: ")
                append(license.displayName)
                appendNewline(2)

                appendLicenseExpiresAt(playerLicense)
                appendNewline(2)

                appendLicenseDependencies(licensePlayer, license.dependencies)
            }
        }
    }

    type {
        notice {
            label { text("Zurück") }
            tooltip { info("Klicke, um zur Übersicht deiner Lizenzen zurückzukehren.") }

            action {
                playerCallback { player ->
                    player.showDialog(myLicensesDialog(licensePlayer))
                }
            }
        }
    }
}
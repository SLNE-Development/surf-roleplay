@file:Suppress("UnstableApiUsage")

package dev.slne.surf.roleplay.mechanic.mechanics.license.dialogs

import dev.slne.surf.roleplay.api.mechanic.license.PlayerLicense
import dev.slne.surf.roleplay.api.mechanic.license.player.LicensePlayer
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.DialogBase
import java.time.format.DateTimeFormatter

private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

fun myLicenseDialog(licensePlayer: LicensePlayer, playerLicense: PlayerLicense): Dialog = dialog {
    base {
        title {
            primary("Lizenz: ")
            append(playerLicense.license.displayName)
        }
        externalTitle { append(playerLicense.license.displayName) }
        afterAction(DialogBase.DialogAfterAction.NONE)

        body {
            plainMessage(400) {
                info("Hier findest du Details zu deiner Lizenz.")
            }
            plainMessage(400) {}
            plainMessage(400) {}
            plainMessage(400) {
                variableKey("Lizenz: ")
                append(playerLicense.license.displayName)
            }

            val expiresAt = playerLicense.expiresAt?.toLocalDateTime()?.format(formatter)
            plainMessage(400) {
                info("Ablaufdatum: ${expiresAt ?: "Unbegrenzt"}")
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
@file:Suppress("UnstableApiUsage")

package dev.slne.surf.roleplay.paper.player.license.dialogs

import dev.slne.surf.roleplay.api.common.player.RpPlayer
import dev.slne.surf.roleplay.api.common.player.identity.RpIdentity
import dev.slne.surf.roleplay.paper.player.license.IdentityLicense
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.DialogBase

fun myLicenseDialog(
    player: RpPlayer,
    identity: RpIdentity,
    identityLicense: IdentityLicense
): Dialog = dialog {
    val license = identityLicense.license

    base {
        title(license.displayName)
        externalTitle { append(license.displayName) }
        afterAction(DialogBase.DialogAfterAction.NONE)

        body {
            plainMessage(400) {
                info("Hier findest du Details zu deiner Lizenz.")
                appendNewline(2)

                variableKey("Lizenz: ")
                append(license.displayName)
                appendNewline(2)

                appendLicenseDescription(license)
                appendNewline(2)

                appendLicenseExpiresAt(identityLicense)
                appendNewline(2)

                appendLicenseDependencies(player, license.dependencies)
            }
        }
    }

    type {
        notice {
            label { text("Zurück") }
            tooltip { info("Klicke, um zur Übersicht deiner Lizenzen zurückzukehren.") }

            action {
                playerCallback {
                    it.showDialog(myLicensesDialog(player, identity))
                }
            }
        }
    }
}
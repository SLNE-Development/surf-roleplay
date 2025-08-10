@file:Suppress("UnstableApiUsage")

package dev.slne.surf.roleplay.paper.player.license.dialogs

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.common.player.RpPlayer
import dev.slne.surf.roleplay.api.common.player.identity.RpIdentity
import dev.slne.surf.roleplay.api.player.license.LicenseService
import dev.slne.surf.roleplay.paper.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import io.papermc.paper.registry.data.dialog.DialogBase

fun buyLicensesDialog(player: RpPlayer, identity: RpIdentity) = dialog {
    val actions = buildDialogList(player, identity)

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

                if (actions.isEmpty()) {
                    appendNewline(2)
                    info("Du hast bereits alle verfügbaren Lizenzen erworben.")
                }
            }
        }
    }

    type {
        if (actions.isEmpty()) {
            notice(backButton(player, identity))
        } else {
            multiAction {
                actions.forEach { action(it) }
                columns(1)
                exitAction(backButton(player, identity))
            }
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
    player: RpPlayer,
    identity: RpIdentity
) = LicenseService.licenses.filterNot {
    identity.hasLicense(it)
}.map { license ->
    actionButton {
        label(license.displayName)
        tooltip { info("Klicke, um zur Detailansicht zu gelangen.") }
        width(300)

        action {
            playerCallback {
                plugin.launch {
                    it.showDialog(buyLicenseDialog(player, identity, license))
                }
            }
        }
    }
}
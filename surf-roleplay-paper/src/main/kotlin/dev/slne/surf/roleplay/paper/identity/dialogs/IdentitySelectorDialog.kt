@file:Suppress("UnstableApiUsage")
@file:OptIn(NmsUseWithCaution::class)

package dev.slne.surf.roleplay.paper.identity.dialogs

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.paper.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.clearDialogs
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.bukkit.api.nms.NmsUseWithCaution
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import io.papermc.paper.registry.data.dialog.DialogBase

fun createIdentitySelectorDialog(player: RpPlayer) = dialog {
    val identities = player.identities

    base {
        title { primary("Identität auswählen") }
        afterAction(DialogBase.DialogAfterAction.WAIT_FOR_RESPONSE)

        body {
            plainMessage(400) {
                info("Wähle eine deiner Identitäten aus, um sie zu verwenden.")
                appendNewline(2)

                info("Du kannst deine Identität mit einem Reconnect wechseln.")

                if (identities.isEmpty()) {
                    appendNewline(2)
                    info("Es gibt keine verfügbaren Identitäten. Eigentlich solltest du diesen Dialog nicht sehen.")
                    appendNewline(1)
                    info("Bitte kontaktiere einen Administrator, um dieses Problem zu beheben.")
                }
            }
        }
    }

    type {
        if (identities.isEmpty()) {
            notice(createCloseButton())
        } else {
            multiAction {
                createIdentitySelectorButtons(player).forEach {
                    action(it)
                }

                columns(1)
                exitAction(createCloseButton())
            }
        }
    }
}

private fun createIdentitySelectorButtons(
    player: RpPlayer,
) = player.identities.map { identity ->
    actionButton {
        label(identity.type.asComponent())
        tooltip {
            info("Klicke, um deine ")
            append(identity.type.asComponent())
            info(" Identität zu verwenden.")
        }
        width(300)

        action {
            playerCallback {
                plugin.launch {
                    player.setActiveIdentity(identity)

                    it.clearDialogs(true)
                }
            }
        }
    }
}

private fun createCloseButton() = actionButton {
    label { text("Schließen") }
    tooltip { info("Schließen") }

    action {
        playerCallback {
            it.clearDialogs(true)
        }
    }
}
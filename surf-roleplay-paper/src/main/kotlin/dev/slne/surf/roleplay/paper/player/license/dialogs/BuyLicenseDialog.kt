@file:Suppress("UnstableApiUsage")

package dev.slne.surf.roleplay.paper.player.license.dialogs

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.common.player.RpPlayer
import dev.slne.surf.roleplay.api.common.player.identity.RpIdentity
import dev.slne.surf.roleplay.paper.player.license.IdentityLicense
import dev.slne.surf.roleplay.paper.player.license.utils.UnobtainableReason
import dev.slne.surf.roleplay.paper.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.DialogBase
import it.unimi.dsi.fastutil.objects.ObjectSet

suspend fun buyLicenseDialog(
    player: RpPlayer,
    identity: RpIdentity,
    license: License,
): Dialog {
    val canObtain = license.canObtain(identity)

    return dialog {
        base {
            title { primary("Lizenzen kaufen") }
            externalTitle(license.displayName)
            afterAction(DialogBase.DialogAfterAction.WAIT_FOR_RESPONSE)

            body {
                plainMessage(400) {
                    info("Hier kannst du die ")
                    append(license.displayName)
                    info(" Lizenz erwerben.")
                    appendNewline(2)

                    variableKey("Kosten: ")
                    variableValue(license.price)
                    appendNewline(2)

                    appendLicenseExpiresAt(IdentityLicense.createFromLicense(license, identity))
                    appendNewline(2)

                    appendLicenseDescription(license)
                    appendNewline(2)

                    appendLicenseDependencies(player, license.dependencies)
                    appendNewline(2)

                    if (canObtain.first) {
                        info("Klicke auf ")
                        variableValue(""""Erwerben"""")
                        info(", um die Lizenz zu kaufen.")
                    } else {
                        error("Du kannst diese Lizenz nicht erwerben:")
                        appendNewline(2)
                        canObtain.second.forEachIndexed { index, reason ->
                            if (index > 0) {
                                appendNewline(2)
                            }

                            reason.message(this)
                        }
                    }
                }
            }
        }

        type {
            if (canObtain.first) {
                confirmation(
                    buyLicenseButton(player, identity, license),
                    backButton(player, identity)
                )
            } else {
                notice(backButton(player, identity))
            }
        }
    }
}

private fun buyLicenseButton(
    player: RpPlayer,
    identity: RpIdentity,
    license: License
) = actionButton {
    label { success("Erwerben") }
    tooltip {
        info("Klicke, um die ")
        append(license.displayName)
        info(" Lizenz zu erwerben.")
    }

    action {
        playerCallback {
            plugin.launch {
                val result = identity.addLicense(license)

                if (!result.success) {
                    it.showDialog(
                        cannotBuyLicenseNotice(
                            player,
                            identity,
                            license,
                            result.reason
                        )
                    )

                    return@launch
                }

                it.showDialog(boughtLicenseNotice(player, identity, result.license!!))
            }
        }
    }
}

private fun boughtLicenseNotice(
    player: RpPlayer,
    identity: RpIdentity,
    license: IdentityLicense
): Dialog = dialog {
    base {
        title { success("Lizenz erworben") }
        afterAction(DialogBase.DialogAfterAction.NONE)

        body {
            plainMessage(400) {
                success("Du hast die Lizenz ")
                append(license.license.displayName)
                success(" erfolgreich erworben.")
            }
        }
    }

    type {
        notice {
            label { text("Zurück") }
            tooltip {
                info("Klicke, um zur ")
                append(license.license.displayName)
                info(" Lizenz zurückzukehren.")
            }

            action {
                playerCallback {
                    it.showDialog(myLicenseDialog(player, identity, license))
                }
            }
        }
    }
}

private fun cannotBuyLicenseNotice(
    player: RpPlayer,
    identity: RpIdentity,
    license: License,
    reason: ObjectSet<UnobtainableReason>,
): Dialog = dialog {
    base {
        title { error("Lizenz kann nicht erworben werden") }
        afterAction(DialogBase.DialogAfterAction.NONE)

        body {
            plainMessage(400) {
                error("Du kannst die ")
                append(license.displayName)
                error(" Lizenz nicht erwerben.")
            }
            plainMessage(400) {}
            plainMessage(400) {
                variableKey("Grund: ")
            }
            plainMessage(400) {
                reason.forEachIndexed { index, unobtainableReason ->
                    if (index > 0) {
                        appendNewline(2)
                    }

                    append(unobtainableReason.message)
                }
            }
        }
    }

    type {
        notice {
            label { text("Zurück") }
            tooltip {
                info("Klicke, um zur ")
                append(license.displayName)
                info(" Lizenz zurückzukehren.")
            }

            action {
                playerCallback {
                    plugin.launch {
                        it.showDialog(buyLicenseDialog(player, identity, license))
                    }
                }
            }
        }
    }
}

private fun backButton(player: RpPlayer, identity: RpIdentity) = actionButton {
    label { text("Zurück") }
    tooltip { info("Klicke, um zur Übersicht der erwerbbaren Lizenzen zurückzukehren.") }

    action {
        playerCallback {
            it.showDialog(buyLicensesDialog(player, identity))
        }
    }
}
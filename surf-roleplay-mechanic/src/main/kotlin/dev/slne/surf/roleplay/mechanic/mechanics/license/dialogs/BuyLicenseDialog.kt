@file:Suppress("UnstableApiUsage")

package dev.slne.surf.roleplay.mechanic.mechanics.license.dialogs

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.api.mechanic.license.License
import dev.slne.surf.roleplay.api.mechanic.license.PlayerLicense
import dev.slne.surf.roleplay.api.mechanic.license.player.LicensePlayer
import dev.slne.surf.roleplay.api.mechanic.license.utils.UnobtainableReason
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.DialogBase

fun buyLicenseDialog(
    licensePlayer: LicensePlayer,
    license: License,
): Dialog = dialog {
    base {
        title { primary("Lizenzen kaufen") }
        externalTitle(license.displayName)
        afterAction(DialogBase.DialogAfterAction.WAIT_FOR_RESPONSE)

        body {
            plainMessage(400) {
                info("Hier kannst du die ")
                append(license.displayName)
                info(" Lizenz erwerben.")
            }
            plainMessage(400) {}
            plainMessage(400) {
                info("Die Lizenz kostet ")
                variableValue(license.price)
                info(".")
            }
            plainMessage(400) {}
            plainMessage(400) {
                variableKey("Voraussetzungen: ")

                if (license.dependencies.isEmpty()) {
                    variableValue("Keine")
                }
            }
            for (dependency in license.dependencies) {
                plainMessage(400) {
                    info("- ")
                    append(dependency.displayName)

                    if (licensePlayer.hasLicense(dependency)) {
                        success(" (Erfüllt)")
                    } else {
                        error(" (Nicht erfüllt)")
                    }
                }
            }
            plainMessage(400) {}
            plainMessage(400) {}
            plainMessage(400) {
                info("Klicke auf ")
                variableValue(""""Erwerben"""")
                info(", um die Lizenz zu kaufen.")
            }
        }
    }

    type {
        multiAction {
            columns(1)
            action(buyLicenseButton(licensePlayer, license))
            exitAction(backButton(licensePlayer))
        }
    }
}

private fun buyLicenseButton(licensePlayer: LicensePlayer, license: License) = actionButton {
    label { success("Erwerben") }
    tooltip {
        info("Klicke, um die ")
        append(license.displayName)
        info(" Lizenz zu erwerben.")
    }
    width(200)

    action {
        playerCallback { player ->
            plugin.launch {
                val result = licensePlayer.addLicense(license)

                if (!result.first) {
                    player.showDialog(
                        cannotBuyLicenseNotice(
                            licensePlayer,
                            license,
                            result.second!!
                        )
                    )

                    return@launch
                }

                player.showDialog(boughtLicenseNotice(licensePlayer, result.third!!))
            }
        }
    }
}

private fun boughtLicenseNotice(
    licensePlayer: LicensePlayer,
    license: PlayerLicense
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
                playerCallback { player ->
                    player.showDialog(myLicenseDialog(licensePlayer, license))
                }
            }
        }
    }
}

private fun cannotBuyLicenseNotice(
    licensePlayer: LicensePlayer,
    license: License,
    reason: UnobtainableReason
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
                reason.message(this)
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
                playerCallback { player ->
                    player.showDialog(buyLicenseDialog(licensePlayer, license))
                }
            }
        }
    }
}

private fun backButton(licensePlayer: LicensePlayer) = actionButton {
    label { text("Zurück") }
    tooltip { info("Klicke, um zur Übersicht der erwerbbaren Lizenzen zurückzukehren.") }

    action {
        playerCallback { player ->
            player.showDialog(buyLicensesDialog(licensePlayer))
        }
    }
}
@file:Suppress("UnstableApiUsage")

package dev.slne.surf.roleplay.mechanic.mechanics.license.dialogs

import dev.slne.surf.roleplay.api.mechanic.license.License
import dev.slne.surf.roleplay.api.mechanic.license.player.LicensePlayer
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import io.papermc.paper.registry.data.dialog.ActionButton
import io.papermc.paper.registry.data.dialog.DialogBase
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.format.TextDecoration

fun SurfComponentBuilder.appendLicenseDependencies(
    licensePlayer: LicensePlayer,
    dependencies: Collection<License>
) {
    variableKey("Voraussetzungen: ")
    if (dependencies.isEmpty()) {
        variableValue("Keine")
    } else {
        appendNewline()
        append(Component.join(JoinConfiguration.newlines(), dependencies.map { dependency ->
            buildText {
                spacer("[")
                if (licensePlayer.hasLicense(dependency)) {
                    success("✔")
                } else {
                    error("✘")
                }
                spacer("] ")
                append(dependency.displayName)
            }
        }))
    }
}

fun licenseDialog(licensePlayer: LicensePlayer) = dialog {
    base {
        title { primary("Lizenzsystem v1.0") }
        afterAction(DialogBase.DialogAfterAction.NONE)

        body {
            plainMessage(400) {
                info("Willkommen im Lizenzsystem! Hier kannst du deine Lizenzen verwalten und erwerben.")
                appendNewline(2)

                info("Klicke auf ")
                variableValue(""""Meine Lizenzen"""")
                info(", um eine Übersicht deiner Lizenzen zu erhalten.")
                appendSpace()

                info("Du kannst Lizenzen erwerben, indem du auf die Schaltfläche ")
                variableValue(""""Lizenz erwerben"""")
                info(" klickst und dann die gewünschte Lizenz auswählst.")
                appendSpace()

                info("Einige Lizenzen können Voraussetzungen haben, die erfüllt sein müssen, bevor du sie erwerben kannst.")

                appendNewline(2)
                error("Achtung: ", TextDecoration.BOLD)
                info("Lizenzen können ablaufen. Überprüfe deine Lizenzen regelmäßig, um sicherzustellen, dass sie noch gültig sind.")
            }
        }
    }

    type {
        multiAction {
            columns(1)
            action(myLicensesButton(licensePlayer))
            action(buyLicensesButton(licensePlayer))
        }
    }
}

private fun myLicensesButton(licensePlayer: LicensePlayer): ActionButton = actionButton {
    label { text("Meine Lizenzen") }
    tooltip { info("Klicke, um deine Lizenzen zu sehen.") }
    width(400)

    action {
        playerCallback { player ->
            player.showDialog(myLicensesDialog(licensePlayer))
        }
    }
}

private fun buyLicensesButton(licensePlayer: LicensePlayer): ActionButton = actionButton {
    label { text("Lizenz erwerben") }
    tooltip { info("Klicke, um eine Lizenz zu erwerben.") }
    width(400)

    action {
        playerCallback { player ->
            player.showDialog(buyLicensesDialog(licensePlayer))
        }
    }
}
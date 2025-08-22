@file:Suppress("UnstableApiUsage")

package dev.slne.surf.roleplay.paper.player.license.dialogs

import dev.slne.surf.roleplay.api.common.player.RpPlayer
import dev.slne.surf.roleplay.api.common.player.identity.RpIdentity
import dev.slne.surf.roleplay.core.common.player.license.IdentityLicense
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import io.papermc.paper.registry.data.dialog.ActionButton
import io.papermc.paper.registry.data.dialog.DialogBase
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.format.TextDecoration
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.map

fun SurfComponentBuilder.appendLicenseDependencies(
    player: RpPlayer,
    dependencies: ObjectSet<License>
) {
    variableKey("Voraussetzungen: ")
    if (dependencies.isEmpty()) {
        variableValue("Keine")
    } else {
        appendNewline()
        append(Component.join(JoinConfiguration.newlines(), dependencies.map { dependency ->
            buildText {
                spacer("[")
                if (player.hasLicense(dependency)) {
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

fun SurfComponentBuilder.appendLicenseDescription(license: License) {
    variableKey("Beschreibung:")
    appendNewline()
    append(license.description)
}

private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

fun SurfComponentBuilder.appendLicenseExpiresAt(playerLicense: IdentityLicense) {
    val (formatted, relative) = calculateExpiresAt(playerLicense)

    variableKey("Ablaufdatum: ")
    variableValue(formatted)

    if (relative != null) {
        spacer(" ($relative)")
    }
}

private fun calculateExpiresAt(
    license: IdentityLicense
): Pair<String, String?> {
    val expiresAt = license.expiresAt?.toLocalDateTime() ?: return "Unbegrenzt" to null
    val formattedDate = expiresAt.format(formatter)

    val now = LocalDateTime.now()
    val relative = buildRelativeString(now, expiresAt)

    return formattedDate to relative
}

private fun buildRelativeString(from: LocalDateTime, to: LocalDateTime): String {
    if (from.compareTo(to) == 0) {
        return "gerade jetzt"
    }

    val duration = if (to.isAfter(from)) {
        Duration.between(from, to)
    } else {
        Duration.between(to, from)
    }

    var seconds = duration.seconds

    val days = seconds / (24 * 3600)
    seconds %= 24 * 3600
    val hours = seconds / 3600
    seconds %= 3600
    val minutes = seconds / 60
    seconds %= 60

    fun part(value: Long, singular: String, plural: String): String? {
        return when (value) {
            0L -> null
            1L -> "1 $singular"
            else -> "$value $plural"
        }
    }

    val parts = listOfNotNull(
        part(days, "Tag", "Tage"),
        part(hours, "Stunde", "Stunden"),
        part(minutes, "Minute", "Minuten"),
        part(seconds, "Sekunde", "Sekunden")
    )

    if (parts.isEmpty()) {
        return if (to.isAfter(from)) "in weniger als einer Sekunde" else "vor weniger als einer Sekunde"
    }

    val joined = parts.joinToString(", ")

    return if (to.isAfter(from)) "in $joined" else "vor $joined"
}

fun licenseDialog(player: RpPlayer, identity: RpIdentity) = dialog {
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
            action(myLicensesButton(player, identity))
            action(buyLicensesButton(player, identity))
        }
    }
}

private fun myLicensesButton(player: RpPlayer, identity: RpIdentity): ActionButton = actionButton {
    label { text("Meine Lizenzen") }
    tooltip { info("Klicke, um deine Lizenzen zu sehen.") }
    width(400)

    action {
        playerCallback {
            it.showDialog(myLicensesDialog(player, identity))
        }
    }
}

private fun buyLicensesButton(player: RpPlayer, identity: RpIdentity): ActionButton = actionButton {
    label { text("Lizenz erwerben") }
    tooltip { info("Klicke, um eine Lizenz zu erwerben.") }
    width(400)

    action {
        playerCallback {
            it.showDialog(buyLicensesDialog(player, identity))
        }
    }
}
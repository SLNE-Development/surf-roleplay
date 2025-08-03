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
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

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

                variableKey("Ablaufdatum: ")
                val (formatted, relative) = calculateExpiresAt(playerLicense)
                variableValue(formatted)
                if (relative != null) {
                    spacer(" ($relative)")
                }
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

private fun calculateExpiresAt(
    playerLicense: PlayerLicense
): Pair<String, String?> {
    val expiresAt = playerLicense.expiresAt?.toLocalDateTime()

    if (expiresAt == null) {
        return "Unbegrenzt" to null
    }

    val formattedDate = expiresAt.format(formatter)

    val now = LocalDateTime.now()
    val relative = buildRelativeString(now, expiresAt)

    return formattedDate to relative
}

private fun buildRelativeString(from: LocalDateTime, to: LocalDateTime): String {
    if (from == to) return "gerade jetzt"

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
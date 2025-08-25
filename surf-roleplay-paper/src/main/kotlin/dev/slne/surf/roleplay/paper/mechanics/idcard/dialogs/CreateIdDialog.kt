@file:Suppress("UnstableApiUsage")
@file:OptIn(NmsUseWithCaution::class)

package dev.slne.surf.roleplay.paper.mechanics.idcard.dialogs

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.roleplay.paper.mechanics.idcard.IdCard
import dev.slne.surf.roleplay.paper.player.identity.identities.CivilianIdentity
import dev.slne.surf.roleplay.paper.player.rpPlayer
import dev.slne.surf.roleplay.paper.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.DialogBodyBuilder
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.clearDialogs
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.bukkit.api.nms.NmsUseWithCaution
import dev.slne.surf.surfapi.core.api.command.builder.CommandExceptionBuilder
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.ActionButton
import io.papermc.paper.registry.data.dialog.DialogBase.DialogAfterAction
import net.kyori.adventure.text.format.TextDecoration
import java.time.LocalDate
import java.time.Year
import java.time.format.DateTimeParseException

private val nameRegex = "^[a-zA-ZÄÖÜäöüß]{3,16}".toRegex()
private val nameRegexDashes = "^[a-zA-ZÄÖÜäöüß-]{3,16}".toRegex()

private const val FIRST_NAME_INPUT = "first_name"
private const val LAST_NAME_INPUT = "last_name"
private const val BIRTH_DATE_INPUT = "birth_date"

fun createIdDialog(
    firstName: String? = null,
    lastName: String? = null,
    birthDateString: String? = null
) = dialog {
    base {
        title { primary("Personalausweis beantragen") }
        afterAction(DialogAfterAction.WAIT_FOR_RESPONSE)

        body {
            plainMessage(400) {
                info("Hier kannst du deinen Personalausweis beantragen.")
            }
            plainMessage(400) {}
            plainMessage(400) {
                error("Achtung: ", TextDecoration.BOLD)
                error("Du kannst deinen Personalausweis im Nachhinein nicht mehr ändern!")
            }
        }

        input {
            text(FIRST_NAME_INPUT) {
                label { text("Vorname") }
                initial = firstName
                maxLength(16)
                width(400)
            }

            text(LAST_NAME_INPUT) {
                label { text("Nachname") }
                initial = lastName
                maxLength(16)
                width(400)
            }

            text(BIRTH_DATE_INPUT) {
                label {
                    text("Geburtsdatum ")
                    spacer("[DD.MM.JJJJ]")
                }
                initial = birthDateString
                maxLength(10)
                width(400)
            }
        }
    }

    type {
        confirmation(confirmCreationButton(), cancelCreationButton())
    }
}

private fun confirmCreationButton(): ActionButton = actionButton {
    label { success("Personalausweis beantragen") }
    tooltip {
        info("Klicke hier, um deinen Personalausweis zu beantragen")
    }

    action {
        customPlayerClick { info, player ->
            val firstName = info.getText(FIRST_NAME_INPUT) ?: ""
            val lastName = info.getText(LAST_NAME_INPUT) ?: ""
            val birthDateString = info.getText(BIRTH_DATE_INPUT) ?: ""

            val isValidFirstNameLength = firstName.matches(nameRegex)
            val isValidLastNameLength = lastName.matches(nameRegex)
            val isValidFirstName = firstName.matches(nameRegexDashes)
            val isValidLastName = lastName.matches(nameRegexDashes)
            val parsedBirthDate = runCatching { LocalDate.parse(birthDateString, IdCard.formatter) }

            val isValidBirthDateString = validateDate(parsedBirthDate)

            if (!isValidBirthDateString || !isValidFirstName || !isValidLastName || !isValidFirstNameLength || !isValidLastNameLength) {
                player.showDialog(
                    invalidInputDialog(
                        firstName,
                        lastName,
                        birthDateString
                    ) {
                        if (!isValidFirstName || !isValidFirstNameLength) {
                            plainMessage(400) {
                                error("Der angegebene Vorname ist ungültig. Bitte gib einen gültigen Vornamen mit ")
                                variableValue("3 bis 16 Buchstaben ")
                                error("ein. Erlaubt sind nur die Buchstaben ")
                                variableValue("a–z, A–Z, ÄÖÜäöüß ")
                                error("sowie ")
                                variableValue("Bindestriche")
                                error(".")
                            }
                        }
                        if (!isValidLastName || !isValidLastNameLength) {
                            plainMessage(400) {
                                error("Der angegebene Nachname ist ungültig. Bitte gib einen gültigen Vornamen mit ")
                                variableValue("3 bis 16 Buchstaben ")
                                error("ein. Erlaubt sind nur die Buchstaben ")
                                variableValue("a–z, A–Z, ÄÖÜäöüß ")
                                error("sowie ")
                                variableValue("Bindestriche")
                                error(".")
                            }
                        }
                        if (!isValidBirthDateString) {
                            val now = LocalDate.now()
                            val validYear = now.minusYears(100)
                            val exception = parsedBirthDate.exceptionOrNull()
                            val exceptionMessage =
                                if (exception != null && exception is DateTimeParseException) {
                                    CommandExceptionBuilder(
                                        exception.message,
                                        exception.parsedString,
                                        exception.errorIndex
                                    ).build(null)
                                } else null

                            plainMessage(400) {
                                error("Das angegebene Geburtsdatum ist ungültig. Bitte gib ein gültiges Datum im folgenden Format ein: ")
                                variableValue("TT.MM.JJJJ")
                                error(".")
                                error(" Dein Geburtsdatum darf außerdem nicht vor ")
                                variableValue(validYear.year)
                                error(" und nicht nach ")
                                variableValue(now.year)
                                error(" liegen.")

                                if (exceptionMessage != null) {
                                    appendNewline(2)
                                    append(exceptionMessage)
                                }
                            }
                        }
                    }
                )
                return@customPlayerClick
            }

            plugin.launch {
                val rpPlayer = player.rpPlayer
                val identity = rpPlayer.createOrUpdateIdentity(
                    CivilianIdentity(
                        uuid = player.uniqueId,
                        firstName = firstName,
                        lastName = lastName,
                        dateOfBirth = parsedBirthDate.getOrThrow()
                    )
                )

                rpPlayer.setActiveIdentity(identity)
                player.showDialog(idCreationSuccess(firstName, lastName, parsedBirthDate.getOrThrow()))
            }
        }
    }
}

private fun invalidInputDialog(
    firstName: String? = null,
    lastName: String? = null,
    birthDate: String? = null,
    errorMessage: DialogBodyBuilder.() -> Unit
): Dialog = dialog {
    base {
        title { error("Der Personalausweis konnte nicht erstellt werden!") }
        afterAction(DialogAfterAction.NONE)

        body {
            plainMessage(400) {}
            errorMessage()
        }
    }

    type {
        notice {
            label { text("Zurück") }
            action {
                playerCallback { player ->
                    player.showDialog(createIdDialog(firstName, lastName, birthDate))
                }
            }
        }
    }
}

private fun idCreationSuccess(
    firstName: String,
    lastName: String,
    birthDate: LocalDate
): Dialog = dialog {
    base {
        title { success("Dein Personalausweises wurde erfolgreich mit folgenden Angaben erstellt:") }
        afterAction(DialogAfterAction.NONE)
        preventClosingWithEscape()

        body {
            plainMessage(400) {
                variableKey("Vorname: ")
                variableValue(firstName)
            }
            plainMessage(400) {
                variableKey("Nachname: ")
                variableValue(lastName)
            }
            plainMessage(400) {
                variableKey("Vorname: ")
                variableValue(IdCard.formatter.format(birthDate))
            }
        }
    }

    type {
        notice {
            label { text("Schließen") }
            action {
                playerCallback { player ->
                    player.clearDialogs()
                    player.inventory.addItem(IdCard.idCard(firstName, lastName, birthDate))
                }
            }
        }
    }

}

private fun cancelCreationButton() = actionButton {
    label { text("Abbrechen") }
    tooltip {
        info("Klicke hier, um den Vorgang abzubrechen")
    }
    action {
        playerCallback { player ->
            player.clearDialogs(true)
        }
    }
}

private fun validateDate(result: Result<LocalDate>): Boolean {
    val date = result.getOrNull() ?: return false

    val currentYear = Year.now().value
    val minYear = currentYear - 100
    return date.year in minYear..currentYear
}

private fun validateDate(dateString: String): Boolean {
    val parts = dateString.split(".")

    if (parts.size != 3) {
        return false
    }

    val dayStr = parts[0]
    val monthStr = parts[1]
    val yearStr = parts[2]

    if (dayStr.length != 2 || monthStr.length != 2 || yearStr.length != 4) {
        return false
    }

    val day = dayStr.toIntOrNull()
    val month = monthStr.toIntOrNull()
    val year = yearStr.toIntOrNull()

    if (day == null || month == null || year == null) {
        return false
    }

    if (day !in 1..31) {
        return false
    }

    if (month !in 1..12) {
        return false
    }

    val currentYear = Year.now().value
    val minYear = currentYear - 100

    if (year !in minYear..currentYear) {
        return false
    }

    return true
}

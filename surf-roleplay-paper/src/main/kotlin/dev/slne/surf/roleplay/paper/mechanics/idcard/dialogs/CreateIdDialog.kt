@file:Suppress("UnstableApiUsage")
@file:OptIn(NmsUseWithCaution::class)

package dev.slne.surf.roleplay.paper.mechanics.idcard.dialogs

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.cloud.api.client.paper.player.toCloudOfflinePlayer
import dev.slne.surf.roleplay.api.common.player.RpPlayer
import dev.slne.surf.roleplay.core.common.player.identity.identities.CivilianIdentityImpl
import dev.slne.surf.roleplay.paper.mechanics.idcard.IdCard
import dev.slne.surf.roleplay.paper.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.DialogBodyBuilder
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.clearDialogs
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.bukkit.api.nms.NmsUseWithCaution
import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.ActionButton
import io.papermc.paper.registry.data.dialog.DialogBase
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player
import java.time.LocalDate
import java.time.Year

private val nameRegex = Regex("^[a-zA-ZÄÖÜäöüß]{3,16}")
private val nameRegexDashes = Regex("^[a-zA-ZÄÖÜäöüß-]{3,16}")

fun createIdDialog(
    firstName: String? = null,
    lastName: String? = null,
    birthDateString: String? = null
) = dialog {
    base {
        title { primary("Personalausweis beantragen") }
        afterAction(DialogBase.DialogAfterAction.WAIT_FOR_RESPONSE)

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
            text("first_name") {
                label { text("Vorname") }
                firstName?.let { initial(it) }
                maxLength(16)
                width(400)
            }
        }
        input {
            text("last_name") {
                label { text("Nachname") }
                lastName?.let { initial(it) }
                maxLength(16)
                width(400)
            }
        }
        input {
            text("birth_date") {
                label {
                    text("Geburtsdatum ")
                    spacer("[DD.MM.JJJJ]")
                }
                birthDateString?.let { initial(it) }
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
        customClick { info, audience ->
            val player = audience as? Player ?: error("Audience is not a Player")

            val firstName = info.getText("first_name") ?: ""
            val lastName = info.getText("last_name") ?: ""
            val birthDateString = info.getText("birth_date") ?: ""

            val isValidFirstNameLength = firstName.matches(nameRegex)
            val isValidLastNameLength = lastName.matches(nameRegex)
            val isValidFirstName = firstName.matches(nameRegexDashes)
            val isValidLastName = lastName.matches(nameRegexDashes)
            val isValidBirthDateString = validateDate(birthDateString)

            if (!isValidBirthDateString || !isValidFirstName || !isValidLastName || !isValidFirstNameLength || !isValidLastNameLength) {
                audience.showDialog(
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
                            plainMessage(400) {
                                error("Das angegebene Geburtsdatum ist ungültig. Bitte gib ein gültiges Datum im folgenden Format ein: ")
                                variableValue("TT.MM.JJJJ")
                                error(".")
                                error(" Dein Geburtsdatum darf außerdem nicht vor ")
                                variableValue(validYear.year)
                                error(" und nicht nach ")
                                variableValue(now.year)
                                error(" liegen.")
                            }
                        }
                    }
                )
                return@customClick
            }

            val birthDate = LocalDate.parse(birthDateString, IdCard.formatter)

            plugin.launch {
                val rpPlayer = RpPlayer[player.toCloudOfflinePlayer()]

                val identity = rpPlayer.createOrUpdateIdentity(
                    CivilianIdentityImpl(
                        player = rpPlayer,
                        firstName = firstName,
                        lastName = lastName,
                        dateOfBirth = birthDate
                    )
                )

                rpPlayer.setActiveIdentity(identity)

                audience.showDialog(idCreationSuccess(firstName, lastName, birthDate))
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
        afterAction(DialogBase.DialogAfterAction.NONE)

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
        afterAction(DialogBase.DialogAfterAction.NONE)

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

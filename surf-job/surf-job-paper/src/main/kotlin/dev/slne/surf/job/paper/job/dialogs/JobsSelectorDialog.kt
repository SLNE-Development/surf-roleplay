@file:Suppress("UnstableApiUsage")
@file:OptIn(NmsUseWithCaution::class)

package dev.slne.surf.job.paper.job.dialogs

import dev.slne.surf.job.api.job.JobRegistry
import dev.slne.surf.job.api.player.JobPlayer
import dev.slne.surf.job.paper.job.jobRegistryImpl
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.clearDialogs
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.bukkit.api.nms.NmsUseWithCaution
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.clickOpensUrl
import dev.slne.surf.surfapi.core.api.util.toObjectList
import io.papermc.paper.registry.data.dialog.DialogBase
import net.kyori.adventure.text.format.TextDecoration

fun createJobsSelectorDialog(player: JobPlayer) = dialog {
    base {
        title { primary("JobSystem ${jobRegistryImpl.VERSION}") }
        afterAction(DialogBase.DialogAfterAction.NONE)

        body {
            plainMessage(400) {
                // schreib eine beschreibung des Job-Systems
                info("Willkommen im Job-System!")
                appendNewline(2)

                info("Hier kannst du dir einen Job aussuchen, der zu dir passt.")
                appendSpace()
                error("Achtung: ", TextDecoration.BOLD)
                info("Jeder Job hat eigene Regeln und Pflichten, die du erfüllen musst.")
                appendSpace()

                info("Informationen zu den Jobs findest du in der ")
                append {
                    variableValue("Dokumentation")

                    hoverEvent(buildText {
                        info("Klicke hier, um die Dokumentation zu öffnen.")
                    })

                    clickOpensUrl("https://server.castcrafter.de")
                }

                info(" oder auf der Detail-Seite des Jobs.")
                appendNewline(2)

                info("Um zu starten, wähle eine Kategorie aus:")
            }
        }
    }

    type {
        multiAction {
            columns(1)
            exitAction(createCloseButton())

            JobRegistry.jobs
                .sortedBy { it.category.sorting }
                .groupBy { it.category }
                .map { (category, jobs) ->
                    category to createJobCategorySelectorDialog(
                        player,
                        category,
                        jobs.toObjectList()
                    )
                }.forEach { (category, dialog) ->
                    action {
                        label(category.asComponent())
                        tooltip(category.asDescriptionComponent())

                        action {
                            playerCallback {
                                it.showDialog(dialog)
                            }
                        }
                    }
                }
        }
    }
}

private fun createCloseButton() = actionButton {
    label { text("Schließen") }
    tooltip { info("Schließen") }
    width(400)

    action {
        playerCallback {
            it.clearDialogs()
        }
    }
}
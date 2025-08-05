@file:Suppress("UnstableApiUsage")

package dev.slne.surf.job.paper.job.dialogs

import dev.slne.surf.job.api.job.Job
import dev.slne.surf.job.api.job.JobCategory
import dev.slne.surf.job.api.player.JobPlayer
import dev.slne.surf.job.paper.job.jobRegistryImpl
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.clickOpensUrl
import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.ActionButton
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet
import it.unimi.dsi.fastutil.objects.ObjectList
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer

fun createJobCategorySelectorDialog(
    player: JobPlayer,
    category: JobCategory,
    jobs: ObjectList<Job>
): Dialog = dialog {
    val jobButtons = buildJobButtons(player, category, jobs)

    base {
        title {
            primary("JobSystem ${jobRegistryImpl.VERSION}")
            spacer(" — ")
            append(category.asComponent())
        }

        body {
            plainMessage(400) {
                info("Hier kannst du dir einen Job aussuchen.")
                appendNewline(2)

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

                if (jobButtons.isEmpty()) {
                    info("Es sind aktuell keine Jobs verfügbar, schau später nochmal vorbei.")
                } else {
                    info("Klicke auf einen der folgenden Jobs, um mehr Informationen zu erhalten:")
                }
            }
        }
    }

    type {
        if (jobButtons.isEmpty()) {
            notice(createCloseButton(player))
        } else {
            multiAction {
                columns(3)

                jobButtons.forEach {
                    action(it)
                }

                exitAction(createCloseButton(player))
            }
        }
    }
}

private fun createCloseButton(player: JobPlayer) = actionButton {
    label { text("Zurück") }
    tooltip { info("Zurück zum Hauptmenü") }
    width(300)

    action {
        playerCallback {
            it.showDialog(createJobsSelectorDialog(player))
        }
    }
}

private fun buildJobButtons(
    player: JobPlayer,
    category: JobCategory,
    jobs: ObjectList<Job>
): ObjectLinkedOpenHashSet<ActionButton> {
    val serializer = PlainTextComponentSerializer.plainText()
    val jobButtons = ObjectLinkedOpenHashSet<ActionButton>()
    val sortedJobs = jobs.sortedBy { serializer.serialize(it.displayName) }

    for (job in sortedJobs) {
        val button = actionButton {
            label {
                text(serializer.serialize(job.displayName))
            }
            tooltip {
                info("Klicke, um mehr Informationen zu erhalten.")
            }
            width(300)

            action {
                playerCallback {
                    it.showDialog(createJobSelectorDialog(player, category, jobs, job))
                }
            }
        }

        jobButtons.add(button)
    }

    return jobButtons
}
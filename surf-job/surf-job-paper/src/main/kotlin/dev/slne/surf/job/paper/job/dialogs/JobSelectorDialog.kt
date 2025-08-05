@file:Suppress("UnstableApiUsage")
@file:OptIn(NmsUseWithCaution::class)

package dev.slne.surf.job.paper.job.dialogs

import dev.slne.surf.job.api.job.Job
import dev.slne.surf.job.api.job.JobCategory
import dev.slne.surf.job.api.player.JobPlayer
import dev.slne.surf.job.paper.job.jobRegistryImpl
import dev.slne.surf.surfapi.bukkit.api.builder.LoreBuilder
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.clearDialogs
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.bukkit.api.nms.NmsUseWithCaution
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.surfapi.core.api.util.toObjectList
import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.ActionButton
import io.papermc.paper.registry.data.dialog.DialogBase
import it.unimi.dsi.fastutil.objects.ObjectList
import net.kyori.adventure.text.Component

fun createJobSelectorDialog(
    player: JobPlayer,
    category: JobCategory,
    jobs: ObjectList<Job>,
    job: Job
) = dialog {
    val canJoin = job.canJoin(player)
    val canJoinRequirements = Job.JobChangeResult.JoinRequirementsNotMet(canJoin)
    val joinRequirements = Job.JobChangeResult.JoinRequirementsNotMet(
        job.joinRequirements.toObjectList()
    )
    val keepRequirements = Job.JobChangeResult.JoinRequirementsNotMet(
        job.keepRequirements.toObjectList()
    )

    base {
        title {
            primary("JobSystem ${jobRegistryImpl.VERSION}")
            spacer(" — ")
            append(category.asComponent())
            spacer(" — ")
            append(job)
        }
        afterAction(DialogBase.DialogAfterAction.NONE)

        body {
            plainMessage(400) {
                info("Hier findest du Informationen zu dem Job ")
                append(job)
                info(".")
                appendNewline(2)

                variableKey("Gehalt: ")
                variableValue(job.formatIncome(player.rpPlayer.bukkitPlayer?.locale()))
                appendNewline(2)

                val description = LoreBuilder().apply(job.description).build()
                val rules = LoreBuilder().apply(job.rules).build()

                variableKey("Beschreibung: ")
                appendNewline()
                if (description.isEmpty()) {
                    variableValue("Keine Beschreibung verfügbar.")
                } else {
                    description.forEachIndexed { index, line ->
                        if (index > 0) {
                            appendNewline()
                        }

                        append(line)
                    }
                }
                appendNewline(3)

                variableKey("Regeln: ")
                appendNewline()
                if (rules.isEmpty()) {
                    variableValue("Keine Regeln verfügbar.")
                } else {
                    rules.forEachIndexed { index, line ->
                        if (index > 0) {
                            appendNewline()
                        }

                        append(line)
                    }
                }
                appendNewline(3)

                variableKey("Beitrittsvoraussetzungen: ")
                appendNewline()
                if (job.joinRequirements.isEmpty()) {
                    variableValue("Keine Beitrittsvoraussetzungen verfügbar.")
                } else {
                    append(joinRequirements.message(false))
                }
                appendNewline(3)

                variableKey("Beibehaltungsbedingungen: ")
                appendNewline()
                if (job.keepRequirements.isEmpty()) {
                    variableValue("Keine Beibehaltungsbedingungen verfügbar.")
                } else {
                    append(keepRequirements.message(false))
                }
                appendNewline(3)

                if (canJoin.isNotEmpty()) {
                    append(canJoinRequirements.message(true))
                }
            }
        }
    }

    type {
        if (canJoin.isEmpty()) {
            confirmation(
                createSelectJobButton(player, category, jobs, job),
                createBackButton(player, category, jobs)
            )
        } else {
            notice(createBackButton(player, category, jobs))
        }
    }
}

private fun createSelectJobButton(
    player: JobPlayer,
    category: JobCategory,
    jobs: ObjectList<Job>,
    job: Job
): ActionButton = actionButton {
    label { success("Job annehmen") }
    tooltip { info("Klicke, um diesen Job anzunehmen.") }

    action {
        playerCallback {
            val result = player.changeJob(job)

            if (result is Job.JobChangeResult.Success) {
                it.showDialog(createJobChangedSuccessNotice(job))

                return@playerCallback
            }

            it.showDialog(
                createJobChangeFailedNotice(
                    player,
                    category,
                    jobs,
                    job,
                    result.message(true)
                )
            )
        }
    }
}

private fun createBackButton(
    player: JobPlayer,
    category: JobCategory,
    jobs: ObjectList<Job>
): ActionButton = actionButton {
    label { text("Zurück") }
    tooltip {
        info("Zurück zur ")
        append(category.asComponent())
        info("-Übersicht.")
    }

    action {
        playerCallback {
            it.showDialog(createJobCategorySelectorDialog(player, category, jobs))
        }
    }
}

private fun createJobChangeFailedNotice(
    player: JobPlayer,
    category: JobCategory,
    jobs: ObjectList<Job>,
    job: Job,
    error: Component
): Dialog =
    dialog {
        base {
            title { primary("Jobwechsel fehlgeschlagen") }
            afterAction(DialogBase.DialogAfterAction.NONE)

            body {
                plainMessage(400) {
                    info("Der Jobwechsel zu ")
                    append(job)
                    info(" ist fehlgeschlagen: ")
                    appendNewline(2)

                    append(error)
                }
            }
        }

        type {
            notice {
                label { text("Zurück") }
                tooltip { info("Zurück zur Detail-Seite des Jobs.") }

                action {
                    playerCallback {
                        it.showDialog(createJobSelectorDialog(player, category, jobs, job))
                    }
                }
            }
        }
    }

private fun createJobChangedSuccessNotice(job: Job): Dialog = dialog {
    base {
        title { primary("Job gewechselt") }
        afterAction(DialogBase.DialogAfterAction.NONE)

        body {
            plainMessage(400) {
                info("Du hast erfolgreich den Job ")
                append(job)
                info(" angenommen. Viel Erfolg bei deinen neuen Aufgaben!")
            }
        }
    }

    type {
        notice {
            label { text("Schließen") }
            tooltip { info("Schließen") }

            action {
                playerCallback {
                    it.clearDialogs()
                }
            }
        }
    }
}
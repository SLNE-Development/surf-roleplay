package dev.slne.surf.roleplay.paper.player.license.utils

import dev.slne.surf.roleplay.paper.player.license.License
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import net.kyori.adventure.text.Component

sealed class UnobtainableReason(message: SurfComponentBuilder.() -> Unit) {
    val message = buildText(message)

    class NotEnoughCash(
        val currentAmount: Int, val neededAmount: Int
    ) : UnobtainableReason({
        error("Du hast nicht genug Bargeld, um diese Lizenz zu erwerben.")
        appendNewline(2)

        variableKey("Dein Bargeld: ")
        variableValue(currentAmount)
        appendNewline(2)

        variableKey("Kosten der Lizenz: ")
        variableValue(neededAmount)
        appendNewline(2)

        info("Du benötigst ${neededAmount - currentAmount} mehr Bargeld, um diese Lizenz zu erwerben.")
    })

    object NoPermissions : UnobtainableReason({
        error("Du hast nicht die nötigen Berechtigungen, um diese Lizenz zu erwerben.")
    })

    object AlreadyHasLicense : UnobtainableReason({
        info("Du hast diese Lizenz bereits erworben.")
    })

    class EventCancelled(
        val reason: String? = null
    ) : UnobtainableReason({
        error("Der Erwerb dieser Lizenz wurde abgebrochen.")
        reason?.let {
            appendNewline(2)
            variableValue("Grund: ")
            variableKey(it)
        }
    })

    class DependenciesNotMet(
        val missingDependencies: List<License>
    ) : UnobtainableReason({
        error("Du erfüllst nicht die Voraussetzungen, um diese Lizenz zu erwerben.")
        appendNewline(2)

        info("Fehlende Lizenzen:")
        appendCollectionNewLine(missingDependencies, Component.empty()) { it.displayName }
    })
}
package dev.slne.surf.roleplay.api.mechanic.license.utils

import dev.slne.surf.roleplay.api.mechanic.license.License
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.text.Component

sealed class UnobtainableReason(message: Component) {
    class NotEnoughCash(
        val currentAmount: Double, val neededAmount: Double
    ) : UnobtainableReason(buildText {
        error("Du hast nicht genug Geld, um diese Lizenz zu erwerben.")
        appendNewline(2)

        variableKey("Dein Bargeld: ")
        variableValue(currentAmount)
        appendNewline(2)

        variableKey("Kosten der Lizenz: ")
        variableValue(neededAmount)
        appendNewline(2)

        info("Du benötigst ${neededAmount - currentAmount} mehr Bargeld, um diese Lizenz zu erwerben.")
    })

    object NoPermissions : UnobtainableReason(buildText {
        error("Du hast nicht die nötigen Berechtigungen, um diese Lizenz zu erwerben.")
    })

    class AlreadyHasLicense : UnobtainableReason(buildText {
        info("Du hast diese Lizenz bereits erworben.")
    })

    class DependenciesNotMet(
        val missingDependencies: ObjectSet<License>
    ) : UnobtainableReason(buildText {
        error("Du erfüllst nicht die Voraussetzungen, um diese Lizenz zu erwerben.")
        appendNewline(2)

        info("Fehlende Lizenzen:")
        appendCollectionNewLine(missingDependencies) { it.displayName }
    })
}
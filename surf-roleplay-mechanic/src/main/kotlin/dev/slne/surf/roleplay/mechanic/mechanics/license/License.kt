package dev.slne.surf.roleplay.mechanic.mechanics.license

import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.mechanic.mechanics.license.player.licensePlayer
import dev.slne.surf.roleplay.mechanic.mechanics.utils.PermissionRegistry
import dev.slne.surf.surfapi.bukkit.api.builder.LoreBuilder
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.text.Component
import java.time.ZonedDateTime
import kotlin.time.Duration

abstract class License(
    val name: String,
    val displayName: Component,
    val description: LoreBuilder.() -> Unit,
    val cost: Double,
    val expiresIn: Duration?,
    val dependencies: ObjectSet<License> = mutableObjectSetOf(),
    val permission: String = PermissionRegistry.create(
        "${PermissionRegistry.PREFIX}.license.${
            name.replace(
                "_",
                "-"
            )
        }"
    ),
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is License) return false

        if (name != other.name) return false

        return true
    }

    override fun hashCode() = name.hashCode()

    suspend fun canObtain(player: RpPlayer): Pair<Boolean, UnobtainableReason?> {
        if (!player.hasPermission(permission)) {
            return false to UnobtainableReason.NoPermissions
        }

        if (player.licensePlayer.hasLicense(this)) {
            return false to UnobtainableReason.AlreadyHasLicense()
        }

        val missingDependencies = dependencies
            .filterNot { player.licensePlayer.hasLicense(it) }
            .toObjectSet()

        if (missingDependencies.isNotEmpty()) {
            return false to UnobtainableReason.DependenciesNotMet(missingDependencies)
        }

        if (!player.hasCashBalance(cost)) {
            return false to UnobtainableReason.NotEnoughCash(
                currentAmount = player.getCashBalance(),
                neededAmount = cost
            )
        }

        return true to null
    }

    sealed class UnobtainableReason(message: Component) {
        class NotEnoughCash(
            val currentAmount: Double, val neededAmount: Double
        ) : UnobtainableReason(buildText {
            error("Du hast nicht genug Geld, um diese Lizenz zu erwerben.")
            appendNewline(2)

            variableKey("Dein Barggeld: ")
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
            info("Du hast diese Lizenz bereits erworben erworben.")
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

    data class PlayerLicense(
        val player: RpPlayer,
        val license: License,
        val expiresAt: ZonedDateTime?
    ) {
        val isExpired get() = expiresAt?.isBefore(ZonedDateTime.now()) ?: false

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is PlayerLicense) return false

            if (player != other.player) return false
            if (license != other.license) return false

            return true
        }

        override fun hashCode(): Int {
            var result = player.hashCode()
            result = 31 * result + license.hashCode()
            return result
        }
    }
}
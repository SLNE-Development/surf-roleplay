package dev.slne.surf.roleplay.mechanic.mechanics.license

import dev.slne.surf.roleplay.api.mechanic.license.License
import dev.slne.surf.roleplay.api.mechanic.license.utils.UnobtainableReason
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.mechanic.mechanics.license.player.licensePlayer
import dev.slne.surf.roleplay.mechanic.mechanics.utils.PermissionRegistry
import dev.slne.surf.surfapi.bukkit.api.builder.LoreBuilder
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import kotlin.time.Duration

abstract class LicenseImpl(
    override val key: Key,
    override val displayName: Component,
    override val description: LoreBuilder.() -> Unit,
    override val cost: Double,
    override val expiresIn: Duration? = null,
    override val dependencies: ObjectSet<License> = objectSetOf(),
    override val permission: String = PermissionRegistry.create(
        "${PermissionRegistry.PREFIX}.license.${
            key.asString().replace("_", "-")
        }"
    )
) : License {
    override suspend fun canObtain(player: RpPlayer): Pair<Boolean, UnobtainableReason?> {
        if (!player.hasPermission(permission)) {
            return false to UnobtainableReason.NoPermissions
        }

        if (player.licensePlayer().hasLicense(this)) {
            return false to UnobtainableReason.AlreadyHasLicense()
        }

        val missingDependencies = dependencies
            .filterNot { player.licensePlayer().hasLicense(it) }
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
}
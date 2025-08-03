package dev.slne.surf.roleplay.mechanic.mechanics.license

import dev.slne.surf.roleplay.api.mechanic.license.License
import dev.slne.surf.roleplay.api.mechanic.license.player.LicensePlayer
import dev.slne.surf.roleplay.api.mechanic.license.utils.UnobtainableReason
import dev.slne.surf.roleplay.mechanic.mechanics.utils.PermissionRegistry
import dev.slne.surf.surfapi.bukkit.api.builder.LoreBuilder
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
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
    override val price: Double,
    override val expiresIn: Duration? = null,
    override val dependencies: ObjectSet<License> = objectSetOf(),
    override val permission: String = PermissionRegistry.create(
        "${PermissionRegistry.PREFIX}.license.${
            key.asString().replace("_", "-")
        }"
    )
) : License {

    override val children
        get() = LicenseMechanicImpl.licenses.filter {
            it.dependencies.contains(
                this
            )
        }.toObjectSet()

    override suspend fun canObtain(player: LicensePlayer): Pair<Boolean, ObjectSet<UnobtainableReason>> {
        val reasons = mutableObjectSetOf<UnobtainableReason>()

        if (!player.rpPlayer.hasPermission(permission)) {
            reasons.add(UnobtainableReason.NoPermissions)
        }

        if (player.hasLicense(this)) {
            reasons.add(UnobtainableReason.AlreadyHasLicense)
        }

        val missingDependencies = dependencies
            .filterNot { player.hasLicense(it) }
            .toObjectSet()

        if (missingDependencies.isNotEmpty()) {
            reasons.add(UnobtainableReason.DependenciesNotMet(missingDependencies))
        }

        if (!player.rpPlayer.hasCashBalance(price)) {
            reasons.add(
                UnobtainableReason.NotEnoughCash(
                    currentAmount = player.rpPlayer.getCashBalance(),
                    neededAmount = price
                )
            )
        }

        return reasons.isEmpty() to reasons
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LicenseImpl) return false

        if (key != other.key) return false

        return true
    }

    override fun hashCode() = key.hashCode()
}
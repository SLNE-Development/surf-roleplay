package dev.slne.surf.roleplay.core.common.player.license

import dev.slne.surf.roleplay.api.common.player.identity.RpIdentity
import dev.slne.surf.roleplay.api.common.player.license.InternalLicenseBridge
import dev.slne.surf.roleplay.api.common.player.license.License
import dev.slne.surf.roleplay.api.common.player.license.utils.UnobtainableReason
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component

abstract class CommonLicense(
    override val key: Key,
    override val displayName: Component,
    override val description: Component,
    override val price: Int,
    override val dependencies: ObjectSet<License> = objectSetOf(),
    override val permission: String = LicensePermissionRegistry.createLicensePermission(key)
) : License {

    private val licenseService get() = InternalLicenseBridge.instance

    override val children
        get() = licenseService.licenses.filter {
            it.dependencies.contains(this)
        }.toObjectSet()

    override suspend fun canObtain(identity: RpIdentity): Pair<Boolean, ObjectSet<UnobtainableReason>> {
        val player = identity.player
        val cloudPlayer = player.cloudPlayer
        val onlineCloudPlayer = cloudPlayer.player

        val reasons = mutableObjectSetOf<UnobtainableReason>()

        if (onlineCloudPlayer != null && !onlineCloudPlayer.hasPermission(permission)) {
            reasons.add(UnobtainableReason.NoPermissions)
        }

        if (identity.hasLicense(this)) {
            reasons.add(UnobtainableReason.AlreadyHasLicense)
        }

        val missingDependencies = dependencies
            .filterNot { identity.hasLicense(it) }
            .toObjectSet()

        if (missingDependencies.isNotEmpty()) {
            reasons.add(UnobtainableReason.DependenciesNotMet(missingDependencies))
        }

        if (!player.hasCashBalance(price)) {
            reasons.add(
                UnobtainableReason.NotEnoughCash(
                    currentAmount = player.getCashBalance(),
                    neededAmount = price
                )
            )
        }

        return reasons.isEmpty() to reasons
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CommonLicense) return false

        if (key != other.key) return false

        return true
    }

    override fun hashCode() = key.hashCode()

}
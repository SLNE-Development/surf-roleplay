package dev.slne.surf.roleplay.paper.player.license

import dev.slne.surf.roleplay.paper.player.identity.RpIdentity
import dev.slne.surf.roleplay.paper.player.license.utils.UnobtainableReason
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.springframework.beans.factory.annotation.Autowired

abstract class License(
    val key: Key,
    val displayName: Component,
    description: SurfComponentBuilder.() -> Unit,
    val price: Int,
    val dependencies: ObjectSet<License> = objectSetOf(),
    val permission: String = LicensePermissionRegistry.createLicensePermission(key)
) {

    @Autowired
    private lateinit var licenseService: PaperLicenseService

    val description = buildText(description)
    val children
        get() = licenseService.all()
            .filter { this in it.dependencies }
            .toObjectSet()

    suspend fun canObtain(identity: RpIdentity) = buildSet {
        val player = identity.player
        val onlineCloudPlayer = player.cloudPlayer.player

        if (onlineCloudPlayer?.hasPermission(permission) != true) {
            add(UnobtainableReason.NoPermissions)
        }

        if (identity.hasLicense(this@License)) {
            add(UnobtainableReason.AlreadyHasLicense)
        }

        val missingDependencies = dependencies.filterNot { identity.hasLicense(it) }
        if (missingDependencies.isNotEmpty()) {
            add(UnobtainableReason.DependenciesNotMet(missingDependencies))
        }

        if (!player.hasCashBalance(price)) {
            add(UnobtainableReason.NotEnoughCash(player.getCashBalance(), price))
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is License) return false

        if (key != other.key) return false

        return true
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }

    override fun toString(): String {
        return "License(key=$key, price=$price, dependencies=$dependencies, permission='$permission', children=$children)"
    }
}
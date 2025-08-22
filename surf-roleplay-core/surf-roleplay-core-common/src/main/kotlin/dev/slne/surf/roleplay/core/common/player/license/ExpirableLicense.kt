package dev.slne.surf.roleplay.core.common.player.license

import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import kotlin.time.Duration

abstract class ExpirableLicense(
    key: Key,
    displayName: Component,
    description: SurfComponentBuilder.() -> Unit,
    price: Int,
    val expiresIn: Duration,
    dependencies: ObjectSet<License> = objectSetOf(),
    permission: String = LicensePermissionRegistry.createLicensePermission(key)
) : License(
    key,
    displayName,
    description,
    price,
    dependencies,
    permission
)
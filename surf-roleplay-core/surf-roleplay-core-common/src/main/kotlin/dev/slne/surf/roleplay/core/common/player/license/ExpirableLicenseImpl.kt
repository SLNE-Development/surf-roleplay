package dev.slne.surf.roleplay.core.common.player.license

import dev.slne.surf.roleplay.api.common.player.license.ExpirableLicense
import dev.slne.surf.roleplay.api.common.player.license.License
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import kotlin.time.Duration

abstract class ExpirableLicenseImpl(
    key: Key,
    displayName: Component,
    description: Component,
    price: Int,
    override val expiresIn: Duration,
    dependencies: ObjectSet<License> = objectSetOf(),
    permission: String = LicensePermissionRegistry.createLicensePermission(key)
) : LicenseImpl(
    key,
    displayName,
    description,
    price,
    dependencies,
    permission
), ExpirableLicense
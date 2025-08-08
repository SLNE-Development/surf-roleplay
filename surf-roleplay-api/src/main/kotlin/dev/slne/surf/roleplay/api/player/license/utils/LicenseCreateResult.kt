package dev.slne.surf.roleplay.api.player.license.utils

import dev.slne.surf.roleplay.api.player.license.IdentityLicense
import it.unimi.dsi.fastutil.objects.ObjectSet

data class LicenseCreateResult(
    val success: Boolean,
    val reason: ObjectSet<UnobtainableReason>,
    val license: IdentityLicense?
)

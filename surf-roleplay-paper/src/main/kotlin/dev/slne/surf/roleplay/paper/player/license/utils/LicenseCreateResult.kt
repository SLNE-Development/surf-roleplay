package dev.slne.surf.roleplay.paper.player.license.utils

import dev.slne.surf.roleplay.paper.player.license.IdentityLicense
import it.unimi.dsi.fastutil.objects.ObjectSet

data class LicenseCreateResult(
    val success: Boolean,
    val reason: Set<UnobtainableReason>,
    val license: IdentityLicense?
)

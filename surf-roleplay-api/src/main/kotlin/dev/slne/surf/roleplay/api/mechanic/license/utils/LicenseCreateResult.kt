package dev.slne.surf.roleplay.api.mechanic.license.utils

import dev.slne.surf.roleplay.api.mechanic.license.PlayerLicense
import it.unimi.dsi.fastutil.objects.ObjectSet

data class LicenseCreateResult(
    val success: Boolean,
    val reason: ObjectSet<UnobtainableReason>,
    val playerLicense: PlayerLicense?
)

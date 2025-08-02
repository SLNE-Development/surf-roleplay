package dev.slne.surf.roleplay.mechanic.mechanics.license.licenses

import dev.slne.surf.roleplay.api.mechanic.license.licenses.FishingLicense
import dev.slne.surf.roleplay.mechanic.mechanics.license.LicenseImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.key
import kotlin.time.Duration.Companion.seconds

object FishingLicenseImpl : LicenseImpl(
    key = key("roleplay", "fishing_license"),
    displayName = buildText { primary("Angelschein") },
    description = {
        line {
            spacer("Die Angel-Lizenz erlaubt es dir, in der Wildnis zu angeln.")
        }
    },
    cost = 500.0,
    expiresIn = 3.seconds
), FishingLicense
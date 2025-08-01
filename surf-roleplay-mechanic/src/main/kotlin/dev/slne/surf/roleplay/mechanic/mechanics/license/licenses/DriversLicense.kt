package dev.slne.surf.roleplay.mechanic.mechanics.license.licenses

import dev.slne.surf.roleplay.mechanic.mechanics.license.License
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object DriversLicense : License(
    name = "drivers_license",
    displayName = buildText { primary("Führerschein") },
    description = {
        line {
            spacer("Der Führerschein erlaubt es dir, Fahrzeuge zu führen.")
        }
    },
    cost = 1000.0,
    expiresIn = null,
)
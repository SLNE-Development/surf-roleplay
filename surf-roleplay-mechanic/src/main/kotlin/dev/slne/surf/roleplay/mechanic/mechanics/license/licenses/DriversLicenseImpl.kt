package dev.slne.surf.roleplay.mechanic.mechanics.license.licenses

import dev.slne.surf.roleplay.api.mechanic.license.licenses.DriversLicense
import dev.slne.surf.roleplay.mechanic.mechanics.license.LicenseImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.key
import net.kyori.adventure.text.Component

object DriversLicenseImpl : LicenseImpl(
    key = key("roleplay", "drivers_license"),
    displayName = Component.text("Führerschein"),
    description = {
        line {
            spacer("Der Führerschein erlaubt es dir, Fahrzeuge zu führen.")
        }
    },
    cost = 1000.0,
    expiresIn = null
), DriversLicense
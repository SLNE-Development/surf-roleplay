package dev.slne.surf.roleplay.paper.player.license.licenses.civilian

import dev.slne.surf.roleplay.paper.player.license.ExpirableLicense
import dev.slne.surf.roleplay.paper.player.license.License
import dev.slne.surf.surfapi.core.api.messages.adventure.key
import dev.slne.surf.surfapi.core.api.messages.adventure.text
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import org.springframework.stereotype.Component
import kotlin.time.Duration.Companion.minutes

@Component
class DriversLicense : License(
    key = key("civilian", "drivers_license"),
    displayName = text("Führerschein"),
    description = {
        spacer("Der Führerschein erlaubt es dir, Fahrzeuge zu führen.")
    },
    price = 1000,
)

@Component
class TruckLicense(
    driversLicense: DriversLicense
) : ExpirableLicense(
    key = key("civilian", "truck_license"),
    displayName = text("Lkw-Führerschein"),
    description = {
        spacer("Der Lkw-Führerschein erlaubt es dir, Lastkraftwagen zu fahren.")
    },
    price = 2000,
    expiresIn = 5.minutes,
    dependencies = objectSetOf(
        driversLicense
    )
)

@Component
class BusLicense(
    driversLicense: DriversLicense
) : License(
    key = key("civilian", "bus_license"),
    displayName = text("Bus-Führerschein"),
    description = {
        spacer("Der Bus-Führerschein erlaubt es dir, Busse zu fahren.")
    },
    price = 2500,
    dependencies = objectSetOf(
        driversLicense
    )
)

@Component
class TaxiLicense(
    driversLicense: DriversLicense
) : License(
    key = key("civilian", "taxi_license"),
    displayName = text("Taxi-Führerschein"),
    description = {
        spacer("Der Taxi-Führerschein erlaubt es dir, Taxis zu fahren.")

    },
    price = 3000,
    dependencies = objectSetOf(
        driversLicense
    )
)

@Component
class MotorcycleLicense : License(
    key = key("civilian", "motorcycle_license"),
    displayName = text("Motorrad-Führerschein"),
    description = {
        spacer("Der Motorrad-Führerschein erlaubt es dir, Motorräder zu fahren.")
    },
    price = 1500,
)

@Component
class BoatLicense : License(
    key = key("civilian", "boat_license"),
    displayName = text("Bootsführerschein"),
    description = {
        spacer("Der Bootsführerschein erlaubt es dir, Boote zu fahren.")
    },
    price = 1800,
)

@Component
class PilotLicense : License(
    key = key("civilian", "pilot_license"),
    displayName = text("Pilotenschein"),
    description = {
        spacer("Der Pilotenschein erlaubt es dir, Flugzeuge zu fliegen.")
    },
    price = 5000,
)

@Component
class HelicopterLicense : License(
    key = key("civilian", "helicopter_license"),
    displayName = text("Hubschrauber-Führerschein"),
    description = {
        spacer("Der Hubschrauber-Führerschein erlaubt es dir, Hubschrauber zu fliegen.")
    },
    price = 6000,
)

package dev.slne.surf.roleplay.core.common.player.license.licenses.civilian

import dev.slne.surf.roleplay.api.common.player.license.licenses.civilian.CivilianVehicleLicense
import dev.slne.surf.roleplay.core.common.player.license.CommonExpirableLicense
import dev.slne.surf.roleplay.core.common.player.license.CommonLicense
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.key
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import net.kyori.adventure.text.Component
import kotlin.time.Duration.Companion.minutes
import org.springframework.stereotype.Component as SpringComponent

class CivilianVehicleLicenseImpl {

    @SpringComponent
    class DriversLicense : CommonLicense(
        key = key("civilian", "drivers_license"),
        displayName = Component.text("Führerschein"),
        description = buildText {
            spacer("Der Führerschein erlaubt es dir, Fahrzeuge zu führen.")
        },
        price = 1000,
    ), CivilianVehicleLicense.DriversLicense

    @SpringComponent
    class TruckLicense(
        driversLicense: CivilianVehicleLicense.DriversLicense
    ) : CommonExpirableLicense(
        key = key("civilian", "truck_license"),
        displayName = Component.text("Lkw-Führerschein"),
        description = buildText {
            spacer("Der Lkw-Führerschein erlaubt es dir, Lastkraftwagen zu fahren.")
        },
        price = 2000,
        expiresIn = 5.minutes,
        dependencies = objectSetOf(
            driversLicense
        )
    ), CivilianVehicleLicense.TruckLicense

    @SpringComponent
    class BusLicense(
        driversLicense: CivilianVehicleLicense.DriversLicense
    ) : CommonLicense(
        key = key("civilian", "bus_license"),
        displayName = Component.text("Bus-Führerschein"),
        description = buildText {
            spacer("Der Bus-Führerschein erlaubt es dir, Busse zu fahren.")
        },
        price = 2500,
        dependencies = objectSetOf(
            driversLicense
        )
    ), CivilianVehicleLicense.BusLicense

    @SpringComponent
    class TaxiLicense(
        driversLicense: DriversLicense
    ) : CommonLicense(
        key = key("civilian", "taxi_license"),
        displayName = Component.text("Taxi-Führerschein"),
        description = buildText {
            spacer("Der Taxi-Führerschein erlaubt es dir, Taxis zu fahren.")

        },
        price = 3000,
        dependencies = objectSetOf(
            driversLicense
        )
    ), CivilianVehicleLicense.TaxiLicense {
    }

    @SpringComponent
    class MotorcycleLicense : CommonLicense(
        key = key("civilian", "motorcycle_license"),
        displayName = Component.text("Motorrad-Führerschein"),
        description = buildText {
            spacer("Der Motorrad-Führerschein erlaubt es dir, Motorräder zu fahren.")
        },
        price = 1500,
    ), CivilianVehicleLicense.MotorcycleLicense

    @SpringComponent
    class BoatLicense : CommonLicense(
        key = key("civilian", "boat_license"),
        displayName = Component.text("Bootsführerschein"),
        description = buildText {
            spacer("Der Bootsführerschein erlaubt es dir, Boote zu fahren.")
        },
        price = 1800,
    ), CivilianVehicleLicense.BoatLicense

    @SpringComponent
    class PilotLicense : CommonLicense(
        key = key("civilian", "pilot_license"),
        displayName = Component.text("Pilotenschein"),
        description = buildText {
            spacer("Der Pilotenschein erlaubt es dir, Flugzeuge zu fliegen.")
        },
        price = 5000,
    ), CivilianVehicleLicense.PilotLicense

    @SpringComponent
    class HelicopterLicense : CommonLicense(
        key = key("civilian", "helicopter_license"),
        displayName = Component.text("Hubschrauber-Führerschein"),
        description = buildText {
            spacer("Der Hubschrauber-Führerschein erlaubt es dir, Hubschrauber zu fliegen.")
        },
        price = 6000,
    ), CivilianVehicleLicense.HelicopterLicense
}
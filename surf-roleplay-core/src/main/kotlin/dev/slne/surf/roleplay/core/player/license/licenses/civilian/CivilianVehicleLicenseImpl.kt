package dev.slne.surf.roleplay.core.player.license.licenses.civilian

import dev.slne.surf.roleplay.api.player.license.License
import dev.slne.surf.roleplay.api.player.license.licenses.civilian.CivilianVehicleLicense
import dev.slne.surf.roleplay.core.player.license.ExpirableLicenseImpl
import dev.slne.surf.roleplay.core.player.license.LicenseImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.key
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import net.kyori.adventure.text.Component
import kotlin.time.Duration.Companion.minutes

object CivilianVehicleLicenseImpl {
    fun register(registration: (License) -> Boolean) {
        registration(DriversLicenseImpl)
        registration(TruckLicenseImpl)
        registration(BusLicenseImpl)
        registration(TaxiLicenseImpl)
        registration(MotorcycleLicenseImpl)
        registration(BoatLicenseImpl)
        registration(PilotLicenseImpl)
    }

    object DriversLicenseImpl : LicenseImpl(
        key = key("roleplay", "drivers_license"),
        displayName = Component.text("Führerschein"),
        description = {
            line {
                spacer("Der Führerschein erlaubt es dir, Fahrzeuge zu führen.")
            }
        },
        price = 1000,
    ), CivilianVehicleLicense.DriversLicense

    object TruckLicenseImpl : ExpirableLicenseImpl(
        key = key("roleplay", "truck_license"),
        displayName = Component.text("Lkw-Führerschein"),
        description = {
            line {
                spacer("Der Lkw-Führerschein erlaubt es dir, Lastkraftwagen zu fahren.")
            }
        },
        price = 2000,
        expiresIn = 5.minutes,
        dependencies = objectSetOf(
            DriversLicenseImpl,
            BusLicenseImpl
        )
    ), CivilianVehicleLicense.TruckLicense

    object BusLicenseImpl : LicenseImpl(
        key = key("roleplay", "bus_license"),
        displayName = Component.text("Bus-Führerschein"),
        description = {
            line {
                spacer("Der Bus-Führerschein erlaubt es dir, Busse zu fahren.")
            }
        },
        price = 2500,
        dependencies = objectSetOf(
            DriversLicenseImpl
        )
    ), CivilianVehicleLicense.BusLicense

    object TaxiLicenseImpl : LicenseImpl(
        key = key("roleplay", "taxi_license"),
        displayName = Component.text("Taxi-Führerschein"),
        description = {
            line {
                spacer("Der Taxi-Führerschein erlaubt es dir, Taxis zu fahren.")
            }
        },
        price = 3000,
        dependencies = objectSetOf(
            DriversLicenseImpl
        )
    ), CivilianVehicleLicense.TaxiLicense

    object MotorcycleLicenseImpl : LicenseImpl(
        key = key("roleplay", "motorcycle_license"),
        displayName = Component.text("Motorrad-Führerschein"),
        description = {
            line {
                spacer("Der Motorrad-Führerschein erlaubt es dir, Motorräder zu fahren.")
            }
        },
        price = 1500,
    ), CivilianVehicleLicense.MotorcycleLicense

    object BoatLicenseImpl : LicenseImpl(
        key = key("roleplay", "boat_license"),
        displayName = Component.text("Bootsführerschein"),
        description = {
            line {
                spacer("Der Bootsführerschein erlaubt es dir, Boote zu fahren.")
            }
        },
        price = 1800,
    ), CivilianVehicleLicense.BoatLicense

    object PilotLicenseImpl : LicenseImpl(
        key = key("roleplay", "pilot_license"),
        displayName = Component.text("Pilotenschein"),
        description = {
            line {
                spacer("Der Pilotenschein erlaubt es dir, Flugzeuge zu fliegen.")
            }
        },
        price = 5000,
    ), CivilianVehicleLicense.PilotLicense
}
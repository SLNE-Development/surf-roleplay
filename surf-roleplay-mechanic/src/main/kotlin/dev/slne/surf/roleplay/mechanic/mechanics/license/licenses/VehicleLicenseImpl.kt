package dev.slne.surf.roleplay.mechanic.mechanics.license.licenses

import dev.slne.surf.roleplay.api.mechanic.license.License
import dev.slne.surf.roleplay.api.mechanic.license.licenses.VehicleLicense
import dev.slne.surf.roleplay.mechanic.mechanics.license.LicenseImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.key
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import net.kyori.adventure.text.Component

object VehicleLicenseImpl {
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
        expiresIn = null
    ), VehicleLicense.DriversLicense

    object TruckLicenseImpl : LicenseImpl(
        key = key("roleplay", "truck_license"),
        displayName = Component.text("Lkw-Führerschein"),
        description = {
            line {
                spacer("Der Lkw-Führerschein erlaubt es dir, Lastkraftwagen zu fahren.")
            }
        },
        price = 2000,
        expiresIn = null,
        dependencies = objectSetOf(
            DriversLicenseImpl,
            BusLicenseImpl
        )
    ), VehicleLicense.TruckLicense

    object BusLicenseImpl : LicenseImpl(
        key = key("roleplay", "bus_license"),
        displayName = Component.text("Bus-Führerschein"),
        description = {
            line {
                spacer("Der Bus-Führerschein erlaubt es dir, Busse zu fahren.")
            }
        },
        price = 2500,
        expiresIn = null,
        dependencies = objectSetOf(
            DriversLicenseImpl
        )
    ), VehicleLicense.BusLicense

    object TaxiLicenseImpl : LicenseImpl(
        key = key("roleplay", "taxi_license"),
        displayName = Component.text("Taxi-Führerschein"),
        description = {
            line {
                spacer("Der Taxi-Führerschein erlaubt es dir, Taxis zu fahren.")
            }
        },
        price = 3000,
        expiresIn = null,
        dependencies = objectSetOf(
            DriversLicenseImpl
        )
    ), VehicleLicense.TaxiLicense

    object MotorcycleLicenseImpl : LicenseImpl(
        key = key("roleplay", "motorcycle_license"),
        displayName = Component.text("Motorrad-Führerschein"),
        description = {
            line {
                spacer("Der Motorrad-Führerschein erlaubt es dir, Motorräder zu fahren.")
            }
        },
        price = 1500,
        expiresIn = null
    ), VehicleLicense.MotorcycleLicense

    object BoatLicenseImpl : LicenseImpl(
        key = key("roleplay", "boat_license"),
        displayName = Component.text("Bootsführerschein"),
        description = {
            line {
                spacer("Der Bootsführerschein erlaubt es dir, Boote zu fahren.")
            }
        },
        price = 1800,
        expiresIn = null
    ), VehicleLicense.BoatLicense

    object PilotLicenseImpl : LicenseImpl(
        key = key("roleplay", "pilot_license"),
        displayName = Component.text("Pilotenschein"),
        description = {
            line {
                spacer("Der Pilotenschein erlaubt es dir, Flugzeuge zu fliegen.")
            }
        },
        price = 5000,
        expiresIn = null
    ), VehicleLicense.PilotLicense
}
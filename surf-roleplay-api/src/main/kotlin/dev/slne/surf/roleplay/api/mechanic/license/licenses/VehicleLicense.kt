package dev.slne.surf.roleplay.api.mechanic.license.licenses

import dev.slne.surf.roleplay.api.mechanic.license.License

interface VehicleLicense {
    interface DriversLicense : License
    interface TruckLicense : License
    interface BusLicense : License
    interface TaxiLicense : License
    interface MotorcycleLicense : License
    interface BoatLicense : License
    interface PilotLicense : License
}
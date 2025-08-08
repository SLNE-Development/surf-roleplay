package dev.slne.surf.roleplay.api.player.license.licenses.civilian

import dev.slne.surf.roleplay.api.player.license.License


interface CivilianVehicleLicense {
    interface DriversLicense : License
    interface TruckLicense : License
    interface BusLicense : License
    interface TaxiLicense : License
    interface MotorcycleLicense : License
    interface BoatLicense : License
    interface PilotLicense : License
}
package dev.slne.surf.roleplay.api.player.license.licenses.police

import dev.slne.surf.roleplay.api.player.license.License

interface PoliceWeaponLicense {
    interface LightWeaponLicense : License
    interface HeavyWeaponLicense : License
}
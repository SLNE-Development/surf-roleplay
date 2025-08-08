package dev.slne.surf.roleplay.api.player.license.licenses.civilian

import dev.slne.surf.roleplay.api.player.license.License

interface CivilianWeaponLicense {
    interface LightWeaponLicense : License
    interface HeavyWeaponLicense : License
}
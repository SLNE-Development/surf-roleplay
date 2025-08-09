package dev.slne.surf.roleplay.api.common.player.license.licenses.civilian

import dev.slne.surf.roleplay.api.common.player.license.License

interface CivilianWeaponLicense {
    interface LightWeaponLicense : License
    interface HeavyWeaponLicense : License
}
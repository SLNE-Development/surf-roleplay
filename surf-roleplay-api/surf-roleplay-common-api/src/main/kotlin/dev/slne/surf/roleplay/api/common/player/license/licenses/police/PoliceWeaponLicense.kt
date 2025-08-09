package dev.slne.surf.roleplay.api.common.player.license.licenses.police

import dev.slne.surf.roleplay.api.common.player.license.License

interface PoliceWeaponLicense {
    interface LightWeaponLicense : License
    interface HeavyWeaponLicense : License
}
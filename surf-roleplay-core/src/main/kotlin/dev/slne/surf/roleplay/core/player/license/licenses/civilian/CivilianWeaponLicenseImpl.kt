package dev.slne.surf.roleplay.core.player.license.licenses.civilian

import dev.slne.surf.roleplay.api.player.license.License
import dev.slne.surf.roleplay.api.player.license.licenses.civilian.CivilianWeaponLicense
import dev.slne.surf.roleplay.core.player.license.LicenseImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.key
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object CivilianWeaponLicenseImpl {
    fun register(registration: (License) -> Boolean) {
        registration(LightWeaponLicenseImpl)
        registration(HeavyWeaponLicenseImpl)
    }

    object LightWeaponLicenseImpl : LicenseImpl(
        key = key("roleplay", "light_weapon_license"),
        displayName = buildText { success("Kleiner Waffenschein") },
        description = {
            line {
                info("Erlaubt das Führen von leichten Waffen wie Pistolen und Revolvern.")
            }
        },
        price = 5000
    ), CivilianWeaponLicense.LightWeaponLicense

    object HeavyWeaponLicenseImpl : LicenseImpl(
        key = key("roleplay", "heavy_weapon_license"),
        displayName = buildText { error("Großer Waffenschein") },
        description = {
            line {
                info("Erlaubt das Führen von schweren Waffen wie Maschinengewehren und Scharfschützengewehren.")
            }
        },
        price = 10000,
        dependencies = objectSetOf(
            LightWeaponLicenseImpl
        )
    ), CivilianWeaponLicense.HeavyWeaponLicense
}
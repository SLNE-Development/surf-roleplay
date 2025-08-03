package dev.slne.surf.roleplay.mechanic.mechanics.license.licenses

import dev.slne.surf.roleplay.api.mechanic.license.License
import dev.slne.surf.roleplay.api.mechanic.license.licenses.WeaponLicense
import dev.slne.surf.roleplay.mechanic.mechanics.license.LicenseImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.key
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object WeaponLicenseImpl {
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
        price = 5000.0
    ), WeaponLicense.LightWeaponLicense

    object HeavyWeaponLicenseImpl : LicenseImpl(
        key = key("roleplay", "heavy_weapon_license"),
        displayName = buildText { error("Großer Waffenschein") },
        description = {
            line {
                info("Erlaubt das Führen von schweren Waffen wie Maschinengewehren und Scharfschützengewehren.")
            }
        },
        price = 10000.0,
        dependencies = objectSetOf(
            LightWeaponLicenseImpl
        )
    ), WeaponLicense.HeavyWeaponLicense
}
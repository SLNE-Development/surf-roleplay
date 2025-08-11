package dev.slne.surf.roleplay.core.common.player.license.licenses.civilian

import dev.slne.surf.roleplay.api.common.player.license.licenses.civilian.CivilianWeaponLicense
import dev.slne.surf.roleplay.core.common.player.license.CommonLicense
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.key
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import org.springframework.stereotype.Component

class CivilianWeaponLicenseImpl {

    @Component
    class LightWeaponLicense : CommonLicense(
        key = key("civilian", "light_weapon_license"),
        displayName = buildText { success("Kleiner Waffenschein") },
        description = buildText {
            spacer("Erlaubt das Führen von leichten Waffen wie Pistolen und Revolvern.")
        },
        price = 5000
    ), CivilianWeaponLicense.LightWeaponLicense

    @Component
    class HeavyWeaponLicense(
        lightWeaponLicense: LightWeaponLicense
    ) : CommonLicense(
        key = key("civilian", "heavy_weapon_license"),
        displayName = buildText { error("Großer Waffenschein") },
        description = buildText {
            spacer("Erlaubt das Führen von schweren Waffen wie Maschinengewehren und Scharfschützengewehren.")
        },
        price = 10000,
        dependencies = objectSetOf(
            lightWeaponLicense
        )
    ), CivilianWeaponLicense.HeavyWeaponLicense
}
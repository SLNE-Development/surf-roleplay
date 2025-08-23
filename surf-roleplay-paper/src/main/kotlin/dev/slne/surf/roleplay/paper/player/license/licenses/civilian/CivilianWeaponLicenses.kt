package dev.slne.surf.roleplay.paper.player.license.licenses.civilian

import dev.slne.surf.roleplay.paper.player.license.License
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.key
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import org.springframework.stereotype.Component


@Component
class LightWeaponLicense : License(
    key = key("civilian", "light_weapon_license"),
    displayName = buildText { success("Kleiner Waffenschein") },
    description = {
        spacer("Erlaubt das Führen von leichten Waffen wie Pistolen und Revolvern.")
    },
    price = 5000
)

@Component
class HeavyWeaponLicense(
    lightWeaponLicense: LightWeaponLicense
) : License(
    key = key("civilian", "heavy_weapon_license"),
    displayName = buildText { error("Großer Waffenschein") },
    description = {
        spacer("Erlaubt das Führen von schweren Waffen wie Maschinengewehren und Scharfschützengewehren.")
    },
    price = 10000,
    dependencies = objectSetOf(
        lightWeaponLicense
    )
)

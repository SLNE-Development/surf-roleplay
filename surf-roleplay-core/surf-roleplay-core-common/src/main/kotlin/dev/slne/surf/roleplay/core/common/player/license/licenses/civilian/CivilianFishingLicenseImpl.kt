package dev.slne.surf.roleplay.core.common.player.license.licenses.civilian

import dev.slne.surf.roleplay.api.common.player.license.licenses.civilian.CivilianFishingLicense
import dev.slne.surf.roleplay.core.common.player.license.LicenseImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.key

object CivilianFishingLicenseImpl : LicenseImpl(
    key = key("roleplay", "fishing_license"),
    displayName = buildText { primary("Angelschein") },
    description = buildText {
        spacer("Die Angel-Lizenz erlaubt es dir, in der Wildnis zu angeln.")
    },
    price = 500,
), CivilianFishingLicense
package dev.slne.surf.roleplay.core.common.player.license.licenses.civilian

import dev.slne.surf.roleplay.api.common.player.license.licenses.civilian.CivilianFishingLicense
import dev.slne.surf.roleplay.core.common.player.license.CommonLicense
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.key
import org.springframework.stereotype.Component

@Component
class CivilianFishingLicense : CommonLicense(
    key = key("civilian", "fishing_license"),
    displayName = buildText { primary("Angelschein") },
    description = buildText {
        spacer("Die Angel-Lizenz erlaubt es dir, in der Wildnis zu angeln.")
    },
    price = 500,
), CivilianFishingLicense
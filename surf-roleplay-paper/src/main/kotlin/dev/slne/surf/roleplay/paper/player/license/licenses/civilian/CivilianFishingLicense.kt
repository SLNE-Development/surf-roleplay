package dev.slne.surf.roleplay.paper.player.license.licenses.civilian

import dev.slne.surf.roleplay.paper.player.license.License
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.key
import org.springframework.stereotype.Component

@Component
class CivilianFishingLicense : License(
    key = key("civilian", "fishing_license"),
    displayName = buildText { primary("Angelschein") },
    description = {
        spacer("Die Angel-Lizenz erlaubt es dir, in der Wildnis zu angeln.")
    },
    price = 500,
)
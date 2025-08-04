package dev.slne.surf.roleplay.mechanic.mechanics.map.maps

import dev.slne.surf.roleplay.api.mechanic.map.maps.RockfordMap
import dev.slne.surf.roleplay.mechanic.mechanics.map.MapImpl
import dev.slne.surf.surfapi.core.api.messages.adventure.key
import dev.slne.surf.surfapi.core.api.util.objectSetOf

class RockfordMapImpl : MapImpl(
    key = key("roleplay_maps", "rockford"),
    name = "rockford",
    displayName = {
        primary("Rockford")
    },
    description = {
        line {
            spacer("Rockford ist eine fiktive Stadt. ")
        }
        line {
            spacer("Sie ist bekannt für ihre dichte Bebauung, vielfältige Architektur")
        }
        line {
            spacer("und eine Mischung aus urbanen und ländlichen Gebieten.")
        }
    },
    rentables = objectSetOf()
), RockfordMap
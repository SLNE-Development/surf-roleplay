package dev.slne.surf.roleplay.core.common.player.identity

import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import net.kyori.adventure.text.ComponentLike

enum class RpIdentityType : ComponentLike {
    /**
     * Represents a player who is a member of the civilian population.
     */
    CIVILIAN {
        override fun asComponent() = buildText {
            text("Zivilist")
        }
    },

    /**
     * Represents a player who is a member of the emergency medical service.
     */
    POLICE {
        override fun asComponent() = buildText {
            text("Polizist")
        }
    },

    /**
     * Represents a player who is a member of the rescue service.
     */
    RESCUE_SERVICE {
        override fun asComponent() = buildText {
            text("SAR")
        }
    }
}
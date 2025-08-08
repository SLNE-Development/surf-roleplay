package dev.slne.surf.roleplay.api.player.identity

import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.license.HasLicenses
import dev.slne.surf.roleplay.api.transaction.HasTransactions
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import net.kyori.adventure.text.ComponentLike
import java.time.LocalDate
import java.time.ZonedDateTime

/**
 * Represents a roleplay identity of a player
 */
interface RpIdentity : HasTransactions, HasLicenses {
    
    /**
     * The player associated with this identity.
     */
    val player: RpPlayer

    /**
     * The type of the identity.
     */
    val type: RpIdentityType

    /**
     * The first name of the player.
     */
    var firstName: String

    /**
     * The last name of the player.
     */
    var lastName: String

    /**
     * The birth date of the player.
     */
    var dateOfBirth: LocalDate

    /**
     * The date and time when the identity was created.
     */
    val createdAt: ZonedDateTime

    /**
     * The date and time when the identity was last updated.
     */
    var updatedAt: ZonedDateTime

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

    interface CivilianIdentity : RpIdentity

    interface RescueServiceIdentity : RpIdentity {
        val rank: String
    }

    interface PoliceIdentity : RpIdentity {
        val rank: String
        val badgeNumber: String
    }
}
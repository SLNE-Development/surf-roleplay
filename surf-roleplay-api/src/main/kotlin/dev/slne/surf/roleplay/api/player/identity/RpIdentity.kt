@file:OptIn(InternalRpApi::class)

package dev.slne.surf.roleplay.api.player.identity

import dev.slne.surf.roleplay.api.player.utils.InternalRpApi
import dev.slne.surf.roleplay.api.transaction.HasTransactions
import java.time.LocalDate
import java.time.ZonedDateTime

/**
 * Represents a roleplay identity of a player
 */
interface RpIdentity : HasTransactions {

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

    enum class RpIdentityType {
        /**
         * Represents a player who is a member of the civilian population.
         */
        CIVILIAN,

        /**
         * Represents a player who is a member of the emergency medical service.
         */
        POLICE,

        /**
         * Represents a player who is a member of the rescue service.
         */
        RESCUE_SERVICE,
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
package dev.slne.surf.roleplay.api.common.player.identity

import dev.slne.surf.roleplay.api.common.player.RpPlayer
import dev.slne.surf.roleplay.api.common.player.license.HasLicenses
import dev.slne.surf.roleplay.api.common.transaction.HasRpTransactions
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import net.kyori.adventure.text.ComponentLike
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.*

/**
 * Represents a roleplay identity of a player
 */
interface RpIdentity : HasRpTransactions, HasLicenses {

    /**
     * The unique identifier for this identity.
     */
    val uuid: UUID

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
     * Updates the information of the current instance of [RpIdentity].
     * The provided [identity] lambda is used to modify the instance's fields.
     *
     * @param identity A lambda that modifies the fields of the current instance of [RpIdentity].
     * @return The updated instance of [RpIdentity].
     */
    suspend fun <T : RpIdentity> updateInformation(identity: T.() -> Unit): T

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

    /**
     * Represents a civilian identity in the roleplay system.
     *
     * This interface is used to distinguish a civilian roleplay identity, which is a type of
     * general roleplay identity. It encapsulates all the properties and behavior associated
     * specifically with civilian players.
     *
     * The `CivilianIdentity` is tied to the broader `RpIdentity` interface and can be manipulated
     * through systems that manage identities, such as creating, updating, or associating them
     * with players. This identity type is expected to represent standard non-authoritative
     * roles within the game.
     */
    interface CivilianIdentity : RpIdentity

    /**
     * Represents an identity for a player who is a member of the rescue service.
     * Extends the generic `RpIdentity` interface to specify details unique to rescue service personnel.
     */
    interface RescueServiceIdentity : RpIdentity {
        /**
         * Represents the rank assigned to a player within the rescue service roleplay identity.
         *
         * This value indicates the hierarchical position or title of the player
         * in the context of their role as a member of the rescue service.
         */
        val rank: String
    }

    /**
     * Represents the identity of a police officer within the game.
     * Extends the base roleplay identity functionality by adding police-specific attributes
     * such as rank and badge number that uniquely define a police officer's identity.
     */
    interface PoliceIdentity : RpIdentity {
        /**
         * Represents the rank of a player in their roleplay identity, such as in a police or rescue service role.
         * This value indicates the hierarchical status or designation within the associated identity.
         */
        val rank: String

        /**
         * Represents the badge number associated with a police identity.
         * The badge number is unique to each police officer and serves as an identifier.
         */
        val badgeNumber: String
    }
}
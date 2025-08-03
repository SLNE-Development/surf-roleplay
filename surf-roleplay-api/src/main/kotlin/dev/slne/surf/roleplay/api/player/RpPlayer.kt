package dev.slne.surf.roleplay.api.player

import dev.slne.surf.roleplay.api.transaction.HasTransactions
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.time.ZonedDateTime
import java.util.*

interface RpPlayer : HasTransactions {

    /**
     * The [UUID] of the player
     */
    val uuid: UUID

    /**
     * The username of the player.
     */
    val username: String?

    /**
     * Information about the player, such as first name, last name, and birth date.
     */
    val information: RpPlayerInformation

    /**
     * The Bukkit [Player] representation of this player, if they are online.
     * Returns `null` if the player is not online.
     */
    val bukkitPlayer: Player?

    /**
     * The Bukkit [OfflinePlayer] representation of this player.
     */
    val bukkitOfflinePlayer: OfflinePlayer

    /**
     * The date and time when the player was created.
     */
    val createdAt: ZonedDateTime

    /**
     * The last time the player's information was updated.
     */
    val updatedAt: ZonedDateTime

    /**
     * Checks if the player has a specific permission.
     *
     * @param permission The permission to check.
     * @return `true` if the player has the permission, `false` otherwise.
     */
    fun hasPermission(permission: String): Boolean = bukkitPlayer?.hasPermission(permission) == true

    /**
     * Updates the player's roleplay information.
     *
     * @param update A lambda function that modifies the player's information.
     */
    suspend fun updateInformation(update: RpPlayerInformation.() -> Unit)

    /**
     * Checks if the player is a citizen.
     * Player is citizen if they have successfully created an IDCard.
     *
     * @return `true` if the player is a citizen, `false` otherwise.
     */
    fun isCitizen(): Boolean

    companion object {
        /**
         * Gets a [RpPlayer] by their [UUID].
         */
        suspend operator fun get(uuid: UUID) = RpPlayerManager[uuid]
    }
}

/**
 * Gets the [RpPlayer] associated with this [Player].
 *
 * @return The [RpPlayer] instance for this player.
 */
suspend fun Player.rpPlayer() = RpPlayer[uniqueId]
package dev.slne.surf.roleplay.api.player

import dev.slne.surf.roleplay.api.player.identity.RpIdentity
import dev.slne.surf.roleplay.api.player.license.HasLicenses
import dev.slne.surf.roleplay.api.transaction.HasTransactions
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.text.ComponentLike
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.time.ZonedDateTime
import java.util.*

interface RpPlayer : HasTransactions, HasLicenses,
    ComponentLike {

    /**
     * The [UUID] of the player
     */
    val uuid: UUID

    /**
     * The username of the player.
     */
    val username: String?

    /**
     * The active identity of the player.
     */
    val activeIdentity: RpIdentity?

    /**
     * The identities of the player.
     */
    val identities: ObjectSet<RpIdentity>

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
     * Sets the active identity for the player.
     *
     * @param identity The [RpIdentity] to set as the active identity.
     */
    suspend fun setActiveIdentity(identity: RpIdentity)

    /**
     * Gets one of the player's identities by type.
     *
     * @param type The type of the identity to retrieve.
     * @return The identity of the specified type, or `null` if not found.
     */
    fun getIdentity(type: RpIdentity.RpIdentityType): RpIdentity?

    /**
     * Gets one of the player's identities by class type.
     *
     * @param clazz The class type of the identity to retrieve.
     * @param T The type of the identity.
     */
    fun <T : RpIdentity> getIdentity(clazz: Class<out T>): RpIdentity?

    /**
     * Checks if the player has a specific permission.
     *
     * @param permission The permission to check.
     * @return `true` if the player has the permission, `false` otherwise.
     */
    fun hasPermission(permission: String): Boolean = bukkitPlayer?.hasPermission(permission) == true

    /**
     * Updates the player's username.
     *
     * @param username The new username to set for the player.
     */
    suspend fun updateUsername(username: String)

    /**
     * Creates a new identity for the player.
     *
     * @param identity The identity to create.
     * @return The created identity.
     */
    suspend fun <T : RpIdentity> createIdentity(identity: T): T

    /**
     * Updates the player's identity information.
     *
     * @param identity the [T]
     * @return The updated identity, or `null` if the identity was not found and `createIfNotExists` is `false`.
     */
    suspend fun <T : RpIdentity> updateIdentity(identity: T): T?

    /**
     * Creates or updates the player's identity.
     *
     * If the identity already exists, it will be updated using the provided lambda function.
     * If it does not exist, a new identity will be created.
     *
     * @param identity the [T]
     * @return The created or updated identity, or `null` if the operation failed.
     */
    suspend fun <T : RpIdentity> createOrUpdateIdentity(identity: T): T

    /**
     * Checks if the player is a citizen.
     *
     * @return `true` if the player is a citizen, `false` otherwise.
     */
    fun hasCompletedCitizenship(): Boolean

    companion object {
        /**
         * Gets a [RpPlayer] by their [UUID].
         */
        suspend operator fun get(uuid: UUID) = RpPlayerManager[uuid]
    }
}

/**
 * Gets the [RpIdentity] of the specified type for this [RpPlayer].
 *
 * @param T The type of the identity to retrieve.
 * @return The identity of the specified type, or `null` if not found.
 */
inline fun <reified T : RpIdentity> RpPlayer.getIdentity() = getIdentity(T::class.java)

/**
 * Gets the [RpPlayer] associated with this [Player].
 *
 * @return The [RpPlayer] instance for this player.
 */
suspend fun Player.rpPlayer() = RpPlayer[uniqueId]
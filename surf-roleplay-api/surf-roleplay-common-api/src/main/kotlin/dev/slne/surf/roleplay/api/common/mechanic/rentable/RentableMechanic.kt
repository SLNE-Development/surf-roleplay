package dev.slne.surf.roleplay.api.common.mechanic.rentable

import dev.slne.surf.roleplay.api.common.mechanic.Mechanic
import dev.slne.surf.roleplay.api.common.player.RpPlayer
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.key.Key
import org.jetbrains.annotations.Unmodifiable

interface RentableMechanic : Mechanic {

    /**
     * The rentables
     */
    val rentables: @Unmodifiable ObjectSet<Rentable>

    /**
     * Adds a rentable
     *
     * @param rentable the [Rentable]
     * @return If adding the rentable was successful
     */
    fun addRentable(rentable: Rentable): Boolean

    /**
     * Collects the rent for a [Rentable]
     *
     * @param rentable the [Rentable] to collect rent for
     * @return the result of the rent collection
     */
    suspend fun collectRent(rentable: Rentable): RentCollectResult

    /**
     * Returns the owned [Rentable]s for one [RpPlayer]
     *
     * @param player the [RpPlayer]
     * @return an object set of [Rentable]s
     */
    fun getOwnedRentablesByPlayer(player: RpPlayer): ObjectSet<Rentable>

    /**
     * Returns the maximum number of ownable rentables for a player.
     *
     * @param player the [RpPlayer] to check
     * @return the maximum number of ownable rentables
     */
    fun getMaxOwnableRentablesByPlayer(player: RpPlayer): Int

    /**
     * Returns a [Rentable] by its key.
     *
     * @param key the [Key] of the [Rentable]
     * @return the [Rentable] if found, or null if not found
     */
    fun getByKey(key: Key): Rentable?

    /**
     * The result of a rent collection attempt.
     */
    enum class RentCollectResult {
        /**
         * The rent was successfully collected.
         */
        SUCCESS,

        /**
         * The rent collection was cancelled, possibly due to an event.
         */
        EVENT_CANCELLED,

        /**
         * The player does not have enough money to pay the rent.
         */
        NOT_ENOUGH_MONEY,

        /**
         * The rentable is not currently rented.
         */
        NOT_RENTED,
    }

}
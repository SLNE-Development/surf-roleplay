package dev.slne.surf.roleplay.core.common.mechanics.rentable

import dev.slne.surf.roleplay.core.common.mechanics.Mechanic
import dev.slne.surf.roleplay.core.common.mechanics.rentable.events.RentableRentCollectEvent
import dev.slne.surf.roleplay.core.common.mechanics.rentable.rentables.House
import dev.slne.surf.roleplay.core.common.player.RpPlayer
import dev.slne.surf.surfapi.core.api.messages.adventure.key
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import net.kyori.adventure.key.Key
import kotlin.time.Duration.Companion.seconds

object RentableMechanic : Mechanic("RentableMechanic") {

    private val _rentables = mutableObjectSetOf<Rentable>()
    val rentables get() = _rentables.freeze()

    /**
     * Adds a rentable
     *
     * @param rentable the [Rentable]
     * @return If adding the rentable was successful
     */
    fun addRentable(rentable: Rentable) = _rentables.add(rentable)

    override suspend fun onEnable() {
        val rentable = House(
            key = key("roleplay_house", "house_1"),
            rent = 1000,
            rentDuration = 10.seconds,
            size = 100.0,
            stories = 3,
            storageContainers = objectSetOf(),
            doors = objectSetOf()
        )

        addRentable(rentable)
    }

    /**
     * Collects the rent for a [Rentable]
     *
     * @param rentable the [Rentable] to collect rent for
     * @return the result of the rent collection
     */
    suspend fun collectRent(rentable: Rentable): RentCollectResult {
        val currentOwner = rentable.owner ?: return RentCollectResult.NOT_RENTED
        val currentBankBalance = currentOwner.getBankBalance()

        val event = RentableRentCollectEvent(
            source = this,
            rentable = rentable,
            amount = rentable.rent
        )

        if (!event.callEvent()) {
            return RentCollectResult.EVENT_CANCELLED
        }

        val rent = event.amount

        if (rent > currentBankBalance) {
            return RentCollectResult.NOT_ENOUGH_MONEY
        }

        currentOwner.removeBankBalance(rent)

        return RentCollectResult.SUCCESS
    }

    /**
     * Returns the owned [Rentable]s for one [RpPlayer]
     *
     * @param player the [RpPlayer]
     * @return an object set of [Rentable]s
     */
    fun getOwnedRentablesByPlayer(player: RpPlayer) =
        _rentables.filter { it.owner == player }.toObjectSet()

    /**
     * Returns the maximum number of ownable rentables for a player.
     *
     * @param player the [RpPlayer] to check
     * @return the maximum number of ownable rentables
     */
    fun getMaxOwnableRentablesByPlayer(player: RpPlayer) = 1

    /**
     * Returns a [Rentable] by its key.
     *
     * @param key the [Key] of the [Rentable]
     * @return the [Rentable] if found, or null if not found
     */
    fun getByKey(key: Key) = _rentables.find { it.key == key }
        ?: _rentables.find { it.key.asString() == key.asString() }
        ?: _rentables.find { it.key.namespace() == key.namespace() && it.key.value() == key.value() }

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
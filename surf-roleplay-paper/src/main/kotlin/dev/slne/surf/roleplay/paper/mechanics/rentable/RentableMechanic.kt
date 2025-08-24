package dev.slne.surf.roleplay.paper.mechanics.rentable

import dev.slne.surf.roleplay.paper.mechanics.AbstractMechanic
import dev.slne.surf.roleplay.paper.mechanics.Mechanic
import dev.slne.surf.roleplay.paper.mechanics.rentable.rentables.House
import dev.slne.surf.roleplay.core.common.player.RpPlayer
import dev.slne.surf.surfapi.core.api.messages.adventure.key
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import net.kyori.adventure.key.Key
import org.springframework.scheduling.annotation.Scheduled
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.seconds

@Mechanic("RentableMechanic")
class RentableMechanic : AbstractMechanic() {

    private val _rentables = mutableObjectSetOf<Rentable>()
    val rentables = _rentables.freeze()

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
            mechanic = this,
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
    @Deprecated("Use rentable.collectRent() instead", ReplaceWith("rentable.collectRent()"))
    suspend fun collectRent(rentable: Rentable): RentCollectResult {
        return rentable.collectRent()
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.SECONDS)
    suspend fun collectRents() {
        val now = ZonedDateTime.now()

        for (rentable in _rentables) {
            if (rentable.owner == null) continue
            val lastCollection = rentable.lastRentCollection
            val delay = rentable.rentDuration

            if (lastCollection == null || now.isAfter(lastCollection.plusSeconds(delay.inWholeSeconds))) {
                rentable.collectRent()
            }
        }
    }


    /**
     * Returns the owned [Rentable]s for one [RpPlayer]
     *
     * @param player the [RpPlayer]
     * @return an object set of [Rentable]s
     */
    fun getOwnedRentablesByPlayer(player: RpPlayer) =
        _rentables.filterTo(mutableObjectSetOf()) { it.owner == player }

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

        /**
         * The rent collection is already in progress.
         */
        ALREADY_COLLECTING
    }
}
package dev.slne.surf.roleplay.core.common.mechanics.rentable

import dev.jorel.commandapi.kotlindsl.*
import dev.slne.surf.roleplay.api.common.mechanic.rentable.Rentable
import dev.slne.surf.roleplay.api.common.mechanic.rentable.RentableMechanic
import dev.slne.surf.roleplay.api.common.player.RpPlayer
import dev.slne.surf.roleplay.api.paper.rentable.events.RentableRentCollectEvent
import dev.slne.surf.roleplay.core.common.mechanics.rentable.rentables.HouseImpl
import dev.slne.surf.roleplay.mechanic.MechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanics.rentable.RentCollectorJob
import dev.slne.surf.roleplay.mechanic.mechanics.rentable.listeners.RentCollectedListener
import dev.slne.surf.roleplay.mechanic.mechanics.rentable.listeners.RentableListener
import dev.slne.surf.roleplay.mechanic.mechanics.rentable.listeners.RentableOnlineListener
import dev.slne.surf.surfapi.core.api.messages.adventure.key
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import net.kyori.adventure.key.Key
import kotlin.time.Duration.Companion.seconds

object RentableMechanicImpl : MechanicImpl(
    name = "RentableMechanic",
    handlers = objectSetOf(
        RentableOnlineListener,
        RentCollectedListener,
        RentableListener
    ),
    rpJobs = objectSetOf(
        RentCollectorJob
    )
), RentableMechanic {

    private val _rentables = mutableObjectSetOf<Rentable>()
    override val rentables get() = _rentables.freeze()

    override fun addRentable(rentable: Rentable) = _rentables.add(rentable)

    override suspend fun onEnable() {
        val rentable = HouseImpl(
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

    override suspend fun collectRent(rentable: Rentable): RentableMechanic.RentCollectResult {
        val currentOwner = rentable.owner ?: return RentableMechanic.RentCollectResult.NOT_RENTED
        val currentBankBalance = currentOwner.getBankBalance()

        val event = RentableRentCollectEvent(
            rentable = rentable,
            amount = rentable.rent
        )

        if (!event.callEvent()) {
            return RentableMechanic.RentCollectResult.EVENT_CANCELLED
        }

        val rent = event.amount

        if (rent > currentBankBalance) {
            return RentableMechanic.RentCollectResult.NOT_ENOUGH_MONEY
        }

        currentOwner.removeBankBalance(rent)

        return RentableMechanic.RentCollectResult.SUCCESS
    }

    override fun getOwnedRentablesByPlayer(player: RpPlayer) =
        _rentables.filter { it.owner == player }.toObjectSet()

    override fun getMaxOwnableRentablesByPlayer(player: RpPlayer) = 1

    override fun getByKey(key: Key) = _rentables.find { it.key == key }
        ?: _rentables.find { it.key.asString() == key.asString() }
        ?: _rentables.find { it.key.namespace() == key.namespace() && it.key.value() == key.value() }
}
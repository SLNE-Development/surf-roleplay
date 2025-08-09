package dev.slne.surf.roleplay.mechanic.mechanics.rentable

import com.github.shynixn.mccoroutine.folia.launch
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.kotlindsl.*
import dev.slne.surf.roleplay.api.mechanic.rentable.Rentable
import dev.slne.surf.roleplay.api.mechanic.rentable.RentableMechanic
import dev.slne.surf.roleplay.api.mechanic.rentable.events.RentableOwnerChangeEvent
import dev.slne.surf.roleplay.api.mechanic.rentable.events.RentableRentCollectEvent
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.api.player.rpPlayer
import dev.slne.surf.roleplay.mechanic.MechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanics.rentable.listeners.RentCollectedListener
import dev.slne.surf.roleplay.mechanic.mechanics.rentable.listeners.RentableListener
import dev.slne.surf.roleplay.mechanic.mechanics.rentable.listeners.RentableOnlineListener
import dev.slne.surf.roleplay.mechanic.mechanics.rentable.rentables.HouseImpl
import dev.slne.surf.roleplay.mechanic.plugin
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.surfapi.core.api.messages.adventure.key
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import net.kyori.adventure.key.Key
import org.bukkit.NamespacedKey
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

        commandAPICommand("rentable") {
            subcommand("rent") {
                namespacedKeyArgument("rentableKey") {
                    replaceSuggestions(ArgumentSuggestions.stringCollection { info ->
                        rentables.map { it.key.asString() }
                    })
                }

                playerExecutor { player, args ->
                    val rentableKey: NamespacedKey by args
                    val rentable = getByKey(rentableKey) ?: run {
                        player.sendText {
                            appendPrefix()

                            error("Es konnte kein Mietobjekt mit dem Key ")
                            variableValue(rentableKey.asString())
                            error(" gefunden werden.")

                        }

                        return@playerExecutor
                    }

                    if (rentable.isRented) {
                        player.sendText {
                            appendPrefix()

                            error("Das Mietobjekt ")
                            variableValue(rentableKey.asString())
                            error(" ist bereits vermietet.")

                            return@playerExecutor
                        }
                    }

                    plugin.launch {
                        val rpPlayer = player.rpPlayer()

                        val result = rentable.setOwner(
                            rpPlayer,
                            RentableOwnerChangeEvent.OwnerChangeReason.RentableBought
                        )

                        if (result is RentableOwnerChangeEvent.OwnerSetResult.Success) {
                            player.sendText {
                                appendPrefix()

                                success("Du hast das Mietobjekt ")
                                variableValue(rentableKey.asString())
                                success(" erfolgreich gemietet.")
                            }
                        } else if (result is RentableOwnerChangeEvent.OwnerSetResult.Failure) {
                            player.sendText {
                                appendPrefix()

                                error("Das Mietobjekt ")
                                variableValue(rentableKey.asString())
                                error(" konnte nicht gemietet werden: ")
                                appendNewline(2)

                                result.reason.message(this)
                            }
                        }
                    }
                }
            }
        }
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
package dev.slne.surf.roleplay.paper.mechanics.rentable

import dev.slne.surf.roleplay.paper.mechanics.rentable.events.RentableMemberAddEvent
import dev.slne.surf.roleplay.paper.mechanics.rentable.events.RentableMemberRemoveEvent
import dev.slne.surf.roleplay.paper.mechanics.rentable.events.RentableOwnerChangeEvent
import dev.slne.surf.roleplay.paper.mechanics.rentable.events.RentableRentCollectEvent
import dev.slne.surf.roleplay.paper.mechanics.rentable.utils.DoorContainer
import dev.slne.surf.roleplay.core.common.player.RpPlayer
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.ComponentLike
import java.time.ZonedDateTime
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.time.Duration

abstract class Rentable(
    val key: Key,
    val rent: Int,
    val rentDuration: Duration,
    val size: Double,
    val stories: Int,
    val mechanic: RentableMechanic,
    storageContainers: ObjectSet<StorageContainer> = objectSetOf(),
    doorContainers: ObjectSet<DoorContainer> = objectSetOf()
) : ComponentLike {

    private val _members = mutableObjectSetOf<RpPlayer>()
    private val _storageContainers = mutableObjectSetOf<StorageContainer>()
    private val _doorContainers = mutableObjectSetOf<DoorContainer>()

    val storageContainers = _storageContainers.freeze()
    val doorContainers = _doorContainers.freeze()

    private val collectingRent = AtomicBoolean(false)


    init {
        _storageContainers.addAll(storageContainers)
        _doorContainers.addAll(doorContainers)
    }

    var owner: RpPlayer? = null
    val members get() = _members.freeze()
    val isRented: Boolean get() = owner != null
    var lastRentCollection: ZonedDateTime? = null

    /**
     * Collects the rent for the rentable property.
     *
     * @return The result of the rent collection, indicating success or failure.
     */
    suspend fun collectRent(): RentableMechanic.RentCollectResult {
        if (!collectingRent.compareAndSet(false, true)) { // exit quickly if already collecting
            return RentableMechanic.RentCollectResult.ALREADY_COLLECTING
        }

        try {
            val currentOwner = owner ?: return RentableMechanic.RentCollectResult.NOT_RENTED
            val currentBankBalance = currentOwner.getBankBalance()

            val event = RentableRentCollectEvent(
                source = this,
                rentable = this,
                amount = rent
            )
            event.post()

            if (event.isCancelled) {
                return RentableMechanic.RentCollectResult.EVENT_CANCELLED
            }

            val rent = event.amount

            if (rent > currentBankBalance) {
                return RentableMechanic.RentCollectResult.NOT_ENOUGH_MONEY
            }

            currentOwner.removeBankBalance(rent)
            lastRentCollection = ZonedDateTime.now()

            return RentableMechanic.RentCollectResult.SUCCESS
        } finally {
            collectingRent.set(false)
        }
    }

    /**
     * Sets the owner of the rentable property and collects the first rent
     *
     * @param player The player to set as the owner of the rentable property.
     * @param reason The reason for the owner change, which can be used to determine the context of the change.
     * @return The result of the owner change event, indicating success or failure.
     */
    suspend fun setOwner(
        player: RpPlayer?,
        reason: RentableOwnerChangeEvent.OwnerChangeReason
    ): RentableOwnerChangeEvent.OwnerSetResult {
        val event = RentableOwnerChangeEvent(this, this, owner, player, reason)
        event.post()

        if (event.isCancelled) {
            return RentableOwnerChangeEvent.OwnerSetResult.Failure(
                RentableOwnerChangeEvent.OwnerSetFailureReason.EventCancelled(
                    event.cancelReason ?: "Unbekannt"
                )
            )
        }

        if (player == null) {
            owner = null
            lastRentCollection = null
            _members.clear()

            return RentableOwnerChangeEvent.OwnerSetResult.Success
        }

        if (player == owner) {
            return RentableOwnerChangeEvent.OwnerSetResult.Failure(
                RentableOwnerChangeEvent.OwnerSetFailureReason.AlreadyOwned
            )
        }

        val maxRentables = mechanic.getMaxOwnableRentablesByPlayer(player)
        val ownedRentables = mechanic.getOwnedRentablesByPlayer(player)

        if (ownedRentables.size >= maxRentables) {
            return RentableOwnerChangeEvent.OwnerSetResult.Failure(
                RentableOwnerChangeEvent.OwnerSetFailureReason.AlreadyOwningTooManyRentables(
                    ownedRentables.size,
                    maxRentables
                )
            )
        }

        val currentMoney = player.getBankBalance()

        if (currentMoney < rent) {
            return RentableOwnerChangeEvent.OwnerSetResult.Failure(
                RentableOwnerChangeEvent.OwnerSetFailureReason.NotEnoughMoney(
                    currentMoney = currentMoney,
                    requiredMoney = rent
                )
            )
        }

        owner = player
        val result = collectRent()

        if (result != RentableMechanic.RentCollectResult.SUCCESS) {
            owner = null

            return RentableOwnerChangeEvent.OwnerSetResult.Failure(
                RentableOwnerChangeEvent.OwnerSetFailureReason.RentCollectionFailed(result)
            )
        }

        return RentableOwnerChangeEvent.OwnerSetResult.Success
    }

    /**
     * Adds a member to the rentable property.
     *
     * @param player The player to add as a member.
     * @return The result of the member addition event, indicating success or failure.
     */
    suspend fun addMember(player: RpPlayer): RentableMemberAddEvent.MemberAddResult {
        if (isMember(player)) {
            return RentableMemberAddEvent.MemberAddResult.Failure(RentableMemberAddEvent.MemberAddFailureReason.AlreadyMember)
        }

        val event = RentableMemberAddEvent(this, this, player)
        event.post()

        if (event.isCancelled) {
            return RentableMemberAddEvent.MemberAddResult.Failure(RentableMemberAddEvent.MemberAddFailureReason.EventCancelled)
        }

        _members.add(player)

        return RentableMemberAddEvent.MemberAddResult.Success
    }

    /**
     * Removes a member from the rentable property.
     *
     * @param player The player to remove from the members.
     * @return The result of the member removal event, indicating success or failure.
     */
    suspend fun removeMember(player: RpPlayer): RentableMemberRemoveEvent.MemberRemoveResult {
        if (!isMember(player)) {
            return RentableMemberRemoveEvent.MemberRemoveResult.Failure(RentableMemberRemoveEvent.MemberRemoveFailureReason.NotMember)
        }

        val event = RentableMemberRemoveEvent(this, this, player)
        event.post()

        if (event.isCancelled) {
            return RentableMemberRemoveEvent.MemberRemoveResult.Failure(RentableMemberRemoveEvent.MemberRemoveFailureReason.EventCancelled)
        }

        _members.remove(player)

        return RentableMemberRemoveEvent.MemberRemoveResult.Success
    }

    /**
     * Checks if the player is a member of the rentable property.
     *
     * @param player The player to check.
     * @return True if the player is a member, false otherwise.
     */
    fun isMember(player: RpPlayer) = _members.contains(player) || owner == player

    override fun asComponent() = buildText {
        variableValue(key.asString())
    }
}
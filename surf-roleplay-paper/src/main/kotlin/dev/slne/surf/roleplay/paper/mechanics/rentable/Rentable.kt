package dev.slne.surf.roleplay.paper.mechanics.rentable

import dev.slne.surf.roleplay.core.common.player.RpPlayer
import dev.slne.surf.roleplay.paper.mechanics.rentable.RentableMechanic.RentCollectResult
import dev.slne.surf.roleplay.paper.mechanics.rentable.events.RentableMemberAddEvent
import dev.slne.surf.roleplay.paper.mechanics.rentable.events.RentableMemberAddEvent.MemberAddFailureReason
import dev.slne.surf.roleplay.paper.mechanics.rentable.events.RentableMemberAddEvent.MemberAddResult
import dev.slne.surf.roleplay.paper.mechanics.rentable.events.RentableMemberRemoveEvent
import dev.slne.surf.roleplay.paper.mechanics.rentable.events.RentableMemberRemoveEvent.MemberRemoveFailureReason
import dev.slne.surf.roleplay.paper.mechanics.rentable.events.RentableMemberRemoveEvent.MemberRemoveResult
import dev.slne.surf.roleplay.paper.mechanics.rentable.events.RentableOwnerChangeEvent
import dev.slne.surf.roleplay.paper.mechanics.rentable.events.RentableOwnerChangeEvent.*
import dev.slne.surf.roleplay.paper.mechanics.rentable.events.RentableRentCollectEvent
import dev.slne.surf.roleplay.paper.mechanics.rentable.utils.DoorContainer
import dev.slne.surf.roleplay.paper.player.PaperRpPlayer
import dev.slne.surf.surfapi.core.api.collection.TransformingObjectSet
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.ComponentLike
import java.time.ZonedDateTime
import java.util.*
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

    private val _members = mutableObjectSetOf<UUID>()
    private val _storageContainers = mutableObjectSetOf<StorageContainer>()
    private val _doorContainers = mutableObjectSetOf<DoorContainer>()
    private val collectingRent = AtomicBoolean(false)

    init {
        _storageContainers.addAll(storageContainers)
        _doorContainers.addAll(doorContainers)
    }

    val storageContainers = _storageContainers.freeze()
    val doorContainers = _doorContainers.freeze()

    private var ownerUuid: UUID? = null
    var owner: PaperRpPlayer?
        get() = ownerUuid?.let(PaperRpPlayer::get)
        set(value) {
            ownerUuid = value?.uuid
        }


    val members = TransformingObjectSet(_members, PaperRpPlayer::get, PaperRpPlayer::uuid).freeze()
    val isRented: Boolean get() = owner != null
    var lastRentCollection: ZonedDateTime? = null

    /**
     * Collects the rent for the rentable property.
     *
     * @return The result of the rent collection, indicating success or failure.
     */
    suspend fun collectRent(): RentCollectResult {
        if (!collectingRent.compareAndSet(false, true)) { // exit quickly if already collecting
            return RentCollectResult.ALREADY_COLLECTING
        }

        try {
            val currentOwner = owner ?: return RentCollectResult.NOT_RENTED
            val currentBankBalance = currentOwner.getBankBalance()

            val event = RentableRentCollectEvent(
                source = this,
                rentable = this,
                amount = rent
            )
            event.post()

            if (event.isCancelled) {
                return RentCollectResult.EVENT_CANCELLED
            }

            val rent = event.amount

            if (rent > currentBankBalance) {
                return RentCollectResult.NOT_ENOUGH_MONEY
            }

            currentOwner.removeBankBalance(rent)
            lastRentCollection = ZonedDateTime.now()

            return RentCollectResult.SUCCESS
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
        player: PaperRpPlayer?,
        reason: OwnerChangeReason
    ): OwnerSetResult {
        val event = RentableOwnerChangeEvent(this, this, owner, player, reason)
        event.post()

        if (event.isCancelled) {
            return OwnerSetResult.Failure(
                OwnerSetFailureReason.EventCancelled(
                    event.cancelReason ?: "Unbekannt"
                )
            )
        }

        if (player == null) {
            ownerUuid = null
            lastRentCollection = null
            _members.clear()

            return OwnerSetResult.Success
        }

        if (player == owner) {
            return OwnerSetResult.Failure(
                OwnerSetFailureReason.AlreadyOwned
            )
        }

        val maxRentables = mechanic.getMaxOwnableRentablesByPlayer(player)
        val ownedRentables = mechanic.getOwnedRentablesByPlayer(player)

        if (ownedRentables.size >= maxRentables) {
            return OwnerSetResult.Failure(
                OwnerSetFailureReason.AlreadyOwningTooManyRentables(
                    ownedRentables.size,
                    maxRentables
                )
            )
        }

        val currentMoney = player.getBankBalance()

        if (currentMoney < rent) {
            return OwnerSetResult.Failure(
                OwnerSetFailureReason.NotEnoughMoney(
                    currentMoney = currentMoney,
                    requiredMoney = rent
                )
            )
        }

        owner = player
        val result = collectRent()

        if (result != RentCollectResult.SUCCESS) {
            owner = null

            return OwnerSetResult.Failure(
                OwnerSetFailureReason.RentCollectionFailed(result)
            )
        }

        return OwnerSetResult.Success
    }

    /**
     * Adds a member to the rentable property.
     *
     * @param player The player to add as a member.
     * @return The result of the member addition event, indicating success or failure.
     */
    suspend fun addMember(player: RpPlayer): MemberAddResult {
        if (isMember(player)) {
            return MemberAddResult.Failure(MemberAddFailureReason.AlreadyMember)
        }

        val event = RentableMemberAddEvent(this, this, player)
        event.post()

        if (event.isCancelled) {
            return MemberAddResult.Failure(MemberAddFailureReason.EventCancelled)
        }

        _members.add(player.uuid)

        return MemberAddResult.Success
    }

    /**
     * Removes a member from the rentable property.
     *
     * @param player The player to remove from the members.
     * @return The result of the member removal event, indicating success or failure.
     */
    suspend fun removeMember(player: RpPlayer): MemberRemoveResult {
        if (!isMember(player)) {
            return MemberRemoveResult.Failure(MemberRemoveFailureReason.NotMember)
        }

        val event = RentableMemberRemoveEvent(this, this, player)
        event.post()

        if (event.isCancelled) {
            return MemberRemoveResult.Failure(MemberRemoveFailureReason.EventCancelled)
        }

        _members.remove(player.uuid)

        return MemberRemoveResult.Success
    }

    /**
     * Checks if the player is a member of the rentable property.
     *
     * @param player The player to check.
     * @return True if the player is a member, false otherwise.
     */
    fun isMember(player: RpPlayer) = _members.contains(player.uuid) || ownerUuid == player.uuid

    override fun asComponent() = buildText {
        variableValue(key.asString())
    }
}
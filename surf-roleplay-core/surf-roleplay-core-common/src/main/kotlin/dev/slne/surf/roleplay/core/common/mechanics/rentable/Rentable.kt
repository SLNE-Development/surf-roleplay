package dev.slne.surf.roleplay.core.common.mechanics.rentable

import dev.slne.surf.roleplay.core.common.mechanics.Mechanic
import dev.slne.surf.roleplay.core.common.mechanics.rentable.events.RentableMemberAddEvent
import dev.slne.surf.roleplay.core.common.mechanics.rentable.events.RentableMemberRemoveEvent
import dev.slne.surf.roleplay.core.common.mechanics.rentable.events.RentableOwnerChangeEvent
import dev.slne.surf.roleplay.core.common.mechanics.rentable.utils.DoorContainer
import dev.slne.surf.roleplay.core.common.player.RpPlayer
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.ComponentLike
import java.time.ZonedDateTime
import kotlin.time.Duration

abstract class Rentable(
    val key: Key,
    val rent: Int,
    val rentDuration: Duration,
    val size: Double,
    val stories: Int,
    storageContainers: ObjectSet<StorageContainer> = objectSetOf(),
    doorContainers: ObjectSet<DoorContainer> = objectSetOf()
) : ComponentLike {

    private val _members = mutableObjectSetOf<RpPlayer>()
    private val _storageContainers = mutableObjectSetOf<StorageContainer>()
    private val _doorContainers = mutableObjectSetOf<DoorContainer>()

    val storageContainers = _storageContainers.freeze()
    val doorContainers = _doorContainers.freeze()

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
        val result = Mechanic.getMechanic<RentableMechanic>().collectRent(this)

        if (result == RentableMechanic.RentCollectResult.SUCCESS) {
            lastRentCollection = ZonedDateTime.now()
        }

        return result
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

        if (!event.callEvent()) {
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

        val rentableMechanic = Mechanic.getMechanic<RentableMechanic>()

        val maxRentables = rentableMechanic.getMaxOwnableRentablesByPlayer(player)
        val ownedRentables = rentableMechanic.getOwnedRentablesByPlayer(player)

        if (ownedRentables.size >= maxRentables) {
            return RentableOwnerChangeEvent.OwnerSetResult.Failure(
                RentableOwnerChangeEvent.OwnerSetFailureReason.AlreadyOwningTooManyRentables(
                    ownedRentables.size,
                    maxRentables
                )
            )
        }

        val currentMoney = player.getBankBalance().toInt()

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
    fun addMember(player: RpPlayer): RentableMemberAddEvent.MemberAddResult {
        if (isMember(player)) {
            return RentableMemberAddEvent.MemberAddResult.Failure(RentableMemberAddEvent.MemberAddFailureReason.AlreadyMember)
        }

        val event = RentableMemberAddEvent(this, this, player)

        if (!event.callEvent()) {
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
    fun removeMember(player: RpPlayer): RentableMemberRemoveEvent.MemberRemoveResult {
        if (!isMember(player)) {
            return RentableMemberRemoveEvent.MemberRemoveResult.Failure(RentableMemberRemoveEvent.MemberRemoveFailureReason.NotMember)
        }

        val event = RentableMemberRemoveEvent(this, this, player)

        if (!event.callEvent()) {
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
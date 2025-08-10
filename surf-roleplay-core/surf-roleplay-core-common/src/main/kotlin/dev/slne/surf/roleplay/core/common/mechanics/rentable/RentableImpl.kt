package dev.slne.surf.roleplay.core.common.mechanics.rentable

import dev.slne.surf.roleplay.api.common.mechanic.Mechanic
import dev.slne.surf.roleplay.api.common.mechanic.rentable.Rentable
import dev.slne.surf.roleplay.api.common.mechanic.rentable.RentableMechanic
import dev.slne.surf.roleplay.api.common.mechanic.rentable.utils.StorageContainer
import dev.slne.surf.roleplay.api.common.mechanic.rentable.utils.door.DoorContainer
import dev.slne.surf.roleplay.api.common.player.RpPlayer
import dev.slne.surf.roleplay.api.mechanic.rentable.events.RentableMemberRemoveEvent
import dev.slne.surf.roleplay.api.mechanic.rentable.events.RentableOwnerChangeEvent
import dev.slne.surf.roleplay.api.paper.rentable.events.RentableMemberAddEvent
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.key.Key
import java.time.ZonedDateTime
import kotlin.time.Duration

abstract class RentableImpl(
    override val key: Key,
    override val rent: Int,
    override val rentDuration: Duration,
    override val size: Double,
    override val stories: Int,
    storageContainers: ObjectSet<StorageContainer> = objectSetOf(),
    doorContainers: ObjectSet<DoorContainer> = objectSetOf()
) : Rentable {

    private val _members = mutableObjectSetOf<RpPlayer>()
    private val _storageContainers = mutableObjectSetOf<StorageContainer>()
    private val _doorContainers = mutableObjectSetOf<DoorContainer>()

    override val storageContainers = _storageContainers.freeze()
    override val doorContainers = _doorContainers.freeze()

    init {
        _storageContainers.addAll(storageContainers)
        _doorContainers.addAll(doorContainers)
    }

    override var owner: RpPlayer? = null
    override val members get() = _members.freeze()
    override val isRented: Boolean get() = owner != null
    override var lastRentCollection: ZonedDateTime? = null

    override suspend fun collectRent(): RentableMechanic.RentCollectResult {
        val result = Mechanic.getMechanic<RentableMechanic>().collectRent(this)

        if (result == RentableMechanic.RentCollectResult.SUCCESS) {
            lastRentCollection = ZonedDateTime.now()
        }

        return result
    }

    override suspend fun setOwner(
        player: RpPlayer?,
        reason: RentableOwnerChangeEvent.OwnerChangeReason
    ): RentableOwnerChangeEvent.OwnerSetResult {
        val event = RentableOwnerChangeEvent(this, owner, player, reason)

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

    override fun addMember(player: RpPlayer): RentableMemberAddEvent.MemberAddResult {
        if (isMember(player)) {
            return RentableMemberAddEvent.MemberAddResult.Failure(RentableMemberAddEvent.MemberAddFailureReason.AlreadyMember)
        }

        val event = RentableMemberAddEvent(this, player)

        if (!event.callEvent()) {
            return RentableMemberAddEvent.MemberAddResult.Failure(RentableMemberAddEvent.MemberAddFailureReason.EventCancelled)
        }

        _members.add(player)

        return RentableMemberAddEvent.MemberAddResult.Success
    }

    override fun removeMember(player: RpPlayer): RentableMemberRemoveEvent.MemberRemoveResult {
        if (!isMember(player)) {
            return RentableMemberRemoveEvent.MemberRemoveResult.Failure(RentableMemberRemoveEvent.MemberRemoveFailureReason.NotMember)
        }

        val event = RentableMemberRemoveEvent(this, player)

        if (!event.callEvent()) {
            return RentableMemberRemoveEvent.MemberRemoveResult.Failure(RentableMemberRemoveEvent.MemberRemoveFailureReason.EventCancelled)
        }

        _members.remove(player)

        return RentableMemberRemoveEvent.MemberRemoveResult.Success
    }

    override fun isMember(player: RpPlayer) = _members.contains(player) || owner == player

    override fun asComponent() = buildText {
        variableValue(key.asString())
    }
}
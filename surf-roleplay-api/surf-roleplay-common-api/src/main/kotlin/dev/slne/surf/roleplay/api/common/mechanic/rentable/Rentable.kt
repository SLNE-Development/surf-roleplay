package dev.slne.surf.roleplay.api.common.mechanic.rentable

import dev.slne.surf.roleplay.api.common.mechanic.rentable.utils.StorageContainer
import dev.slne.surf.roleplay.api.common.mechanic.rentable.utils.door.Door
import dev.slne.surf.roleplay.api.common.mechanic.rentable.utils.door.DoorContainer
import dev.slne.surf.roleplay.api.common.mechanic.rentable.utils.events.RentableMemberAddEvent
import dev.slne.surf.roleplay.api.common.mechanic.rentable.utils.events.RentableMemberRemoveEvent
import dev.slne.surf.roleplay.api.common.mechanic.rentable.utils.events.RentableOwnerChangeEvent
import dev.slne.surf.roleplay.api.common.player.RpPlayer
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.ComponentLike
import org.jetbrains.annotations.Unmodifiable
import java.time.ZonedDateTime
import kotlin.time.Duration

/**
 * Represents a rentable property in the game.
 * A rentable property can be owned, rented, and have members associated with it.
 */
interface Rentable : ComponentLike {

    /**
     * The unique identifier for the rentable property.
     */
    val key: Key

    /**
     * The name of the rentable property.
     */
    val rent: Int

    /**
     * The duration for which the property can be rented, after this the rent will be due again.
     */
    val rentDuration: Duration

    /**
     * The date and time when the rent was last collected.
     */
    val lastRentCollection: ZonedDateTime?

    /**
     * The size of the rentable property in square blocks.
     */
    val size: Double

    /**
     * The number of stories in the rentable property.
     */
    val stories: Int

    /**
     * The owner of the rentable property, if any.
     */
    val owner: RpPlayer?

    /**
     * The members of the rentable property, if any.
     */
    val members: @Unmodifiable ObjectSet<RpPlayer>

    /**
     * The [StorageContainer]s of this rentable
     */
    val storageContainers: @Unmodifiable ObjectSet<StorageContainer>

    /**
     * The [Door]s of this rentable
     */
    val doorContainers: @Unmodifiable ObjectSet<DoorContainer>

    /**
     * Checks if the property is currently rented.
     */
    val isRented: Boolean

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
    ): RentableOwnerChangeEvent.OwnerSetResult

    /**
     * Adds a member to the rentable property.
     *
     * @param player The player to add as a member.
     * @return The result of the member addition event, indicating success or failure.
     */
    fun addMember(player: RpPlayer): RentableMemberAddEvent.MemberAddResult

    /**
     * Removes a member from the rentable property.
     *
     * @param player The player to remove from the members.
     * @return The result of the member removal event, indicating success or failure.
     */
    fun removeMember(player: RpPlayer): RentableMemberRemoveEvent.MemberRemoveResult

    /**
     * Checks if the player is a member of the rentable property.
     *
     * @param player The player to check.
     * @return True if the player is a member, false otherwise.
     */
    fun isMember(player: RpPlayer): Boolean

    /**
     * Collects the rent for the rentable property.
     *
     * @return The result of the rent collection, indicating success or failure.
     */
    suspend fun collectRent(): RentableMechanic.RentCollectResult
}
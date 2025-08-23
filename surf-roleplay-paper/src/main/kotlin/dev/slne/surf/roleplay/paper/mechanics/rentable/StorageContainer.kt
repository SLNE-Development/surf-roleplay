@file:Suppress("UnstableApiUsage")

package dev.slne.surf.roleplay.paper.mechanics.rentable

import dev.slne.surf.cloud.api.common.player.teleport.WorldLocation
import dev.slne.surf.roleplay.paper.mechanics.rentable.utils.Crackable
import dev.slne.surf.roleplay.paper.mechanics.rentable.utils.Lockable
import dev.slne.surf.roleplay.core.common.player.RpPlayer
import dev.slne.surf.surfapi.core.api.generated.BlockTypeKeys
import net.kyori.adventure.key.Key
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

abstract class StorageContainer(
    val rentable: Rentable,
    val location: WorldLocation,
    val type: StorageContainerType,
    val size: StorageContainerSize
) : Lockable {

    override fun canAccess(player: RpPlayer): Boolean {
        val crackableAndCracked = this is Crackable && this.cracked

        return crackableAndCracked || rentable.isMember(player)
    }

    /**
     * The different types of storage containers.
     */
    enum class StorageContainerType(val blockType: Key) {
        REGULAR(BlockTypeKeys.CHEST),
        VAULT(BlockTypeKeys.VAULT);
    }

    /**
     * The size of the storage container, which determines how many slots it has.
     *
     * @property size The number of slots in the storage container.
     * @property vaultCrackDuration The duration the vault will be cracked if being cracked
     */
    enum class StorageContainerSize(val size: Int, val vaultCrackDuration: Duration) {
        /**
         * A 9x1 storage container, resulting in 9 slots.
         */
        SIZE_9X1(9, 30.seconds),

        /**
         * A 9x2 storage container, resulting in 18 slots.
         */
        SIZE_9X2(18, 30.seconds),

        /**
         * A 9x3 storage container, resulting in 27 slots.
         */
        SIZE_9X3(27, 60.seconds),

        /**
         * A 9x4 storage container, resulting in 36 slots.
         */
        SIZE_9X4(36, 60.seconds),

        /**
         * A 9x5 storage container, resulting in 45 slots.
         */
        SIZE_9X5(45, 90.seconds),

        /**
         * A 9x6 storage container, resulting in 54 slots.
         */
        SIZE_9X6(54, 90.seconds), ;
    }
}
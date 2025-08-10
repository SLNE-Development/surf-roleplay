package dev.slne.surf.roleplay.api.common.mechanic.rentable.utils

import dev.slne.surf.roleplay.api.common.mechanic.rentable.Rentable
import org.bukkit.Location
import org.bukkit.Material
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Represents a storage container that can be placed in the world and is associated with a rentable property.
 */
interface StorageContainer {

    /**
     * The rentable property that this storage container belongs to.
     */
    val rentable: Rentable

    /**
     * The location of the storage container in the world.
     */
    val location: Location

    /**
     * The type of the storage container, which can be either a regular storage container or a vault.
     */
    val type: StorageContainerType

    /**
     * The size of the storage container, which determines how many slots it has.
     */
    val size: StorageContainerSize

    /**
     * Places the storage container in the world at its designated location.
     */
    suspend fun placeInWorld()

    /**
     * The different types of storage containers.
     */
    enum class StorageContainerType(val material: Material) {
        REGULAR(Material.CHEST),
        VAULT(Material.VAULT);
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
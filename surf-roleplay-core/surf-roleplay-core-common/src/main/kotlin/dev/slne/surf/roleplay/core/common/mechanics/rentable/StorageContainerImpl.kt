@file:Suppress("UnstableApiUsage")

package dev.slne.surf.roleplay.mechanic.mechanics.rentable.utils

import com.github.shynixn.mccoroutine.folia.regionDispatcher
import dev.slne.surf.roleplay.api.mechanic.rentable.Rentable
import dev.slne.surf.roleplay.api.mechanic.rentable.utils.Crackable
import dev.slne.surf.roleplay.api.mechanic.rentable.utils.Lockable
import dev.slne.surf.roleplay.api.mechanic.rentable.utils.StorageContainer
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.core.common.mechanics.plugin
import dev.slne.surf.surfapi.bukkit.api.util.getBlockAtAsync
import kotlinx.coroutines.withContext
import org.bukkit.Location

open class StorageContainerImpl(
    override val rentable: Rentable,
    override val location: Location,
    override val type: StorageContainer.StorageContainerType,
    override val size: StorageContainer.StorageContainerSize
) : StorageContainer, Lockable {

    override suspend fun placeInWorld() = withContext(plugin.regionDispatcher(location)) {
        val block = location.world.getBlockAtAsync(location.toBlock())

        block.type = type.material
    }

    override fun canAccess(player: RpPlayer): Boolean {
        val crackableAndCracked = this is Crackable && this.cracked

        return crackableAndCracked || rentable.isMember(player)
    }
}
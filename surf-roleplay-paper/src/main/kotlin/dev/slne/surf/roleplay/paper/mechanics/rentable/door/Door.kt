package dev.slne.surf.roleplay.paper.mechanics.rentable.door

import com.github.shynixn.mccoroutine.folia.regionDispatcher
import dev.slne.surf.cloud.api.client.paper.toLocation
import dev.slne.surf.cloud.api.common.player.teleport.WorldLocation
import dev.slne.surf.roleplay.paper.mechanics.rentable.Cracker
import dev.slne.surf.roleplay.paper.mechanics.rentable.Rentable
import dev.slne.surf.roleplay.paper.mechanics.rentable.lockpick.LockPick
import dev.slne.surf.roleplay.paper.mechanics.rentable.utils.Crackable
import dev.slne.surf.roleplay.paper.mechanics.rentable.utils.DoorContainer
import dev.slne.surf.roleplay.paper.mechanics.rentable.utils.Lockable
import dev.slne.surf.roleplay.core.common.player.RpPlayer
import dev.slne.surf.roleplay.paper.plugin
import dev.slne.surf.surfapi.bukkit.api.util.getBlockAtAsync
import dev.slne.surf.surfapi.core.api.util.mapAsync
import kotlinx.coroutines.withContext
import org.bukkit.Location
import kotlin.collections.forEach
import kotlin.time.Duration
import org.bukkit.block.data.type.Door as DoorBlockData

@Suppress("UnstableApiUsage")
abstract class Door(
    val rentable: Rentable,
    val container: DoorContainer,
    val location: Location,
    override val crackDuration: Duration
) : Lockable, Crackable {

    override var cracked: Boolean = false

    private suspend fun doorStateOperation(
        door: Door,
        operation: (DoorBlockData) -> Unit
    ) {
        val doorLocation = door.location

        withContext(plugin.regionDispatcher(doorLocation)) {
            val doorBlock = doorLocation.world.getBlockAtAsync(doorLocation.toBlock())
            val doorBlockState = doorBlock.state

            if (doorBlockState is DoorBlockData) {
                operation(doorBlockState)
                doorBlockState.update(true, false)
            }
        }
    }

    suspend fun open() {
        container.doors.mapAsync { door ->
            doorStateOperation(door) { state ->
                state.isOpen = true
            }
        }
    }

    suspend fun close() {
        container.doors.mapAsync { door ->
            doorStateOperation(door) { state ->
                state.isOpen = false
            }
        }
    }

    override fun canAccess(player: RpPlayer) = cracked || rentable.isMember(player)

    override suspend fun crack(
        player: RpPlayer,
        lockPick: LockPick
    ) = Cracker.crack(player, this, lockPick) {
        cracked = true
    }
}
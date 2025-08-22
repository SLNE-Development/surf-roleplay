package dev.slne.surf.roleplay.core.common.mechanics.rentable.door

import dev.slne.surf.cloud.api.common.player.teleport.WorldLocation
import dev.slne.surf.roleplay.core.common.mechanics.rentable.Cracker
import dev.slne.surf.roleplay.core.common.mechanics.rentable.Rentable
import dev.slne.surf.roleplay.core.common.mechanics.rentable.lockpick.LockPick
import dev.slne.surf.roleplay.core.common.mechanics.rentable.utils.Crackable
import dev.slne.surf.roleplay.core.common.mechanics.rentable.utils.DoorContainer
import dev.slne.surf.roleplay.core.common.mechanics.rentable.utils.Lockable
import dev.slne.surf.roleplay.core.common.player.RpPlayer
import kotlin.time.Duration

abstract class Door(
    val rentable: Rentable,
    val container: DoorContainer,
    val location: WorldLocation,
    override val crackDuration: Duration
) : Lockable, Crackable {

    override var cracked: Boolean = false

    private suspend fun doorStateOperation(
        door: Door,
        operation: (org.bukkit.block.data.type.Door) -> Unit
    ) {
        val doorLocation = door.location

        val doorBlock = doorLocation.world.getBlockAtAsync(doorLocation.toBlock())
        val doorBlockState = doorBlock.state

        if (doorBlockState is org.bukkit.block.data.type.Door) {
            operation(doorBlockState)
            doorBlockState.update(true, false)
        }
    }

    suspend fun open() {
        container.doors.forEach { door ->
            doorStateOperation(door) { state ->
                state.isOpen = true
            }
        }
    }

    suspend fun close() {
        container.doors.forEach { door ->
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
@file:Suppress("UnstableApiUsage")

package dev.slne.surf.roleplay.mechanic.mechanics.rentable.utils

import dev.slne.surf.roleplay.api.mechanic.rentable.Rentable
import dev.slne.surf.roleplay.api.mechanic.rentable.utils.LockPick
import dev.slne.surf.roleplay.api.mechanic.rentable.utils.door.Door
import dev.slne.surf.roleplay.api.mechanic.rentable.utils.door.DoorContainer
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.surfapi.bukkit.api.util.getBlockAtAsync
import org.bukkit.Location
import kotlin.time.Duration

abstract class DoorImpl(
    override val rentable: Rentable,
    override val container: DoorContainer,
    override val location: Location,
    override val crackDuration: Duration
) : Door {

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

    override suspend fun open() {
        container.doors.forEach { door ->
            doorStateOperation(door) { state ->
                state.isOpen = true
            }
        }
    }

    override suspend fun close() {
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
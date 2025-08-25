package dev.slne.surf.roleplay.paper.mechanics.rentable.containers

import dev.slne.surf.cloud.api.common.player.teleport.WorldLocation
import dev.slne.surf.roleplay.paper.mechanics.rentable.Cracker
import dev.slne.surf.roleplay.paper.mechanics.rentable.Rentable
import dev.slne.surf.roleplay.paper.mechanics.rentable.StorageContainer
import dev.slne.surf.roleplay.paper.mechanics.rentable.lockpick.LockPick
import dev.slne.surf.roleplay.paper.mechanics.rentable.utils.Crackable
import dev.slne.surf.roleplay.core.common.player.RpPlayer
import kotlin.time.Duration

class VaultStorageContainer(
    rentable: Rentable,
    location: WorldLocation,
    size: StorageContainerSize,
    override val crackDuration: Duration = size.vaultCrackDuration
) : StorageContainer(
    rentable = rentable,
    location = location,
    type = StorageContainerType.VAULT,
    size = size,
), Crackable {
    override var cracked: Boolean = false

    override suspend fun crack(
        player: RpPlayer,
        lockPick: LockPick
    ) = Cracker.crack(player, this, lockPick) {
        cracked = true
    }
}
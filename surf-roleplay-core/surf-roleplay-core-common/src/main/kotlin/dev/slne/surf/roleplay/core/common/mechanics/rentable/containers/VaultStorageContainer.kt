package dev.slne.surf.roleplay.core.common.mechanics.rentable.containers

import dev.slne.surf.roleplay.api.common.mechanic.rentable.Rentable
import dev.slne.surf.roleplay.api.common.mechanic.rentable.utils.Crackable
import dev.slne.surf.roleplay.api.common.mechanic.rentable.utils.LockPick
import dev.slne.surf.roleplay.api.common.mechanic.rentable.utils.StorageContainer
import dev.slne.surf.roleplay.api.common.player.RpPlayer
import dev.slne.surf.roleplay.core.common.mechanics.rentable.Cracker
import dev.slne.surf.roleplay.mechanic.mechanics.rentable.utils.StorageContainerImpl
import org.bukkit.Location
import kotlin.time.Duration

class VaultStorageContainer(
    rentable: Rentable,
    location: Location,
    size: StorageContainer.StorageContainerSize,
    override val crackDuration: Duration = size.vaultCrackDuration
) : StorageContainerImpl(
    rentable = rentable,
    location = location,
    type = StorageContainer.StorageContainerType.VAULT,
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
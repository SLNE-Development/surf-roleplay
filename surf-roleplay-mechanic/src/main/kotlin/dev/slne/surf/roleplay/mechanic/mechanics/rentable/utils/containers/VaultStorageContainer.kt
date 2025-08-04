package dev.slne.surf.roleplay.mechanic.mechanics.rentable.utils.containers

import dev.slne.surf.roleplay.api.mechanic.rentable.Rentable
import dev.slne.surf.roleplay.api.mechanic.rentable.utils.Crackable
import dev.slne.surf.roleplay.api.mechanic.rentable.utils.LockPick
import dev.slne.surf.roleplay.api.mechanic.rentable.utils.StorageContainer
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.roleplay.mechanic.mechanics.rentable.utils.Cracker
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

    override fun canAccess(player: RpPlayer) = cracked || rentable.isMember(player)
}
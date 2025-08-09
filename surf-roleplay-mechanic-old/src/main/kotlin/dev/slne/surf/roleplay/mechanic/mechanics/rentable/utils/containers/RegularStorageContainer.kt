package dev.slne.surf.roleplay.mechanic.mechanics.rentable.utils.containers

import dev.slne.surf.roleplay.api.mechanic.rentable.Rentable
import dev.slne.surf.roleplay.api.mechanic.rentable.utils.StorageContainer
import dev.slne.surf.roleplay.mechanic.mechanics.rentable.utils.StorageContainerImpl
import org.bukkit.Location

class RegularStorageContainer(
    rentable: Rentable,
    location: Location,
    size: StorageContainer.StorageContainerSize
) : StorageContainerImpl(
    rentable = rentable,
    location = location,
    type = StorageContainer.StorageContainerType.REGULAR,
    size = size,
)
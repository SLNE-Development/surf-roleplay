package dev.slne.surf.roleplay.core.common.mechanics.rentable.containers

import dev.slne.surf.cloud.api.common.player.teleport.WorldLocation
import dev.slne.surf.roleplay.core.common.mechanics.rentable.Rentable
import dev.slne.surf.roleplay.core.common.mechanics.rentable.StorageContainer

class RegularStorageContainer(
    rentable: Rentable,
    location: WorldLocation,
    size: StorageContainer.StorageContainerSize
) : StorageContainer(
    rentable = rentable,
    location = location,
    type = StorageContainer.StorageContainerType.REGULAR,
    size = size,
)
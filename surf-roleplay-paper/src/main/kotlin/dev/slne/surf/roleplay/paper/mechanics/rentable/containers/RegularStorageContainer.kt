package dev.slne.surf.roleplay.paper.mechanics.rentable.containers

import dev.slne.surf.cloud.api.common.player.teleport.WorldLocation
import dev.slne.surf.roleplay.paper.mechanics.rentable.Rentable
import dev.slne.surf.roleplay.paper.mechanics.rentable.StorageContainer

class RegularStorageContainer(
    rentable: Rentable,
    location: WorldLocation,
    size: StorageContainerSize
) : StorageContainer(
    rentable = rentable,
    location = location,
    type = StorageContainerType.REGULAR,
    size = size,
)
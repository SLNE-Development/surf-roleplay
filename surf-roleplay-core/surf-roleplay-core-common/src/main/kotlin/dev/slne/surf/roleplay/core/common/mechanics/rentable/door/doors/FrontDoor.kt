package dev.slne.surf.roleplay.core.common.mechanics.rentable.door.doors

import dev.slne.surf.cloud.api.common.player.teleport.WorldLocation
import dev.slne.surf.roleplay.core.common.mechanics.rentable.Rentable
import dev.slne.surf.roleplay.core.common.mechanics.rentable.door.Door
import dev.slne.surf.roleplay.core.common.mechanics.rentable.utils.DoorContainer
import kotlin.time.Duration.Companion.seconds

class FrontDoor(
    rentable: Rentable,
    container: DoorContainer,
    location: WorldLocation
) : Door(
    rentable = rentable,
    container = container,
    location = location,
    crackDuration = 30.seconds
) 
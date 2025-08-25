package dev.slne.surf.roleplay.paper.mechanics.rentable.door.doors

import dev.slne.surf.cloud.api.common.player.teleport.WorldLocation
import dev.slne.surf.roleplay.paper.mechanics.rentable.Rentable
import dev.slne.surf.roleplay.paper.mechanics.rentable.door.Door
import dev.slne.surf.roleplay.paper.mechanics.rentable.utils.DoorContainer
import org.bukkit.Location
import kotlin.time.Duration.Companion.seconds

class GarageDoor(
    rentable: Rentable,
    container: DoorContainer,
    location: Location
) : Door(
    rentable = rentable,
    container = container,
    location = location,
    crackDuration = 15.seconds
) 
package dev.slne.surf.roleplay.core.common.mechanics.rentable.door.doors

import dev.slne.surf.roleplay.api.common.mechanic.rentable.Rentable
import dev.slne.surf.roleplay.api.common.mechanic.rentable.utils.door.DoorContainer
import dev.slne.surf.roleplay.core.common.mechanics.rentable.door.DoorImpl
import org.bukkit.Location
import kotlin.time.Duration.Companion.seconds

class FrontDoor(
    rentable: Rentable,
    container: DoorContainer,
    location: Location
) : DoorImpl(
    rentable = rentable,
    container = container,
    location = location,
    crackDuration = 30.seconds
) 
package dev.slne.surf.roleplay.mechanic.mechanics.rentable.utils.doors

import dev.slne.surf.roleplay.api.mechanic.rentable.Rentable
import dev.slne.surf.roleplay.api.mechanic.rentable.utils.door.DoorContainer
import dev.slne.surf.roleplay.mechanic.mechanics.rentable.utils.DoorImpl
import org.bukkit.Location
import kotlin.time.Duration.Companion.seconds

class GarageDoor(
    rentable: Rentable,
    container: DoorContainer,
    location: Location
) : DoorImpl(
    rentable = rentable,
    container = container,
    location = location,
    crackDuration = 15.seconds
) 
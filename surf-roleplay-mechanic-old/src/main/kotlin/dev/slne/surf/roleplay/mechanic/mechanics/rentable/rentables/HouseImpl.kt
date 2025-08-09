package dev.slne.surf.roleplay.mechanic.mechanics.rentable.rentables

import dev.slne.surf.roleplay.api.mechanic.rentable.rentables.House
import dev.slne.surf.roleplay.api.mechanic.rentable.utils.StorageContainer
import dev.slne.surf.roleplay.api.mechanic.rentable.utils.door.DoorContainer
import dev.slne.surf.roleplay.mechanic.mechanics.rentable.RentableImpl
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.key.Key
import kotlin.time.Duration

class HouseImpl(
    key: Key,
    rent: Int,
    rentDuration: Duration,
    size: Double,
    stories: Int,
    storageContainers: ObjectSet<StorageContainer> = mutableObjectSetOf(),
    doors: ObjectSet<DoorContainer> = mutableObjectSetOf()
) : RentableImpl(
    key,
    rent,
    rentDuration,
    size,
    stories,
    storageContainers,
    doors
), House
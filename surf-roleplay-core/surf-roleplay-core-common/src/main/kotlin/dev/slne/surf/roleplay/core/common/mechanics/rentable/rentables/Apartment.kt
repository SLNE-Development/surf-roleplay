package dev.slne.surf.roleplay.core.common.mechanics.rentable.rentables

import dev.slne.surf.roleplay.core.common.mechanics.rentable.Rentable
import dev.slne.surf.roleplay.core.common.mechanics.rentable.StorageContainer
import dev.slne.surf.roleplay.core.common.mechanics.rentable.utils.DoorContainer
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.key.Key
import kotlin.time.Duration

class Apartment(
    key: Key,
    rent: Int,
    rentDuration: Duration,
    size: Double,
    stories: Int,
    storageContainers: ObjectSet<StorageContainer> = mutableObjectSetOf(),
    doors: ObjectSet<DoorContainer> = mutableObjectSetOf()
) : Rentable(
    key,
    rent,
    rentDuration,
    size,
    stories,
    storageContainers,
    doors
)
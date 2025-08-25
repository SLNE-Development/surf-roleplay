package dev.slne.surf.roleplay.paper.mechanics.rentable.rentables

import dev.slne.surf.roleplay.paper.mechanics.rentable.Rentable
import dev.slne.surf.roleplay.paper.mechanics.rentable.RentableMechanic
import dev.slne.surf.roleplay.paper.mechanics.rentable.StorageContainer
import dev.slne.surf.roleplay.paper.mechanics.rentable.utils.DoorContainer
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.key.Key
import kotlin.time.Duration

class House(
    key: Key,
    rent: Int,
    rentDuration: Duration,
    size: Double,
    stories: Int,
    mechanic: RentableMechanic,
    storageContainers: ObjectSet<StorageContainer> = mutableObjectSetOf(),
    doors: ObjectSet<DoorContainer> = mutableObjectSetOf()
) : Rentable(
    key,
    rent,
    rentDuration,
    size,
    stories,
    mechanic,
    storageContainers,
    doors
)
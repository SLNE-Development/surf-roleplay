package dev.slne.surf.roleplay.core.common.mechanics.rentable.utils

import dev.slne.surf.roleplay.core.common.mechanics.rentable.door.Door
import it.unimi.dsi.fastutil.objects.ObjectSet

/**
 * Represents a container that holds a set of doors.
 */
interface DoorContainer {
    /**
     * A set of doors associated with the container.
     */
    val doors: ObjectSet<Door>
}
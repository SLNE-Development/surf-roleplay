package dev.slne.surf.roleplay.api.mechanic.rentable.utils.door

import it.unimi.dsi.fastutil.objects.ObjectSet

interface DoorContainer {
    /**
     * A set of doors associated with the container.
     */
    val doors: ObjectSet<Door>
}
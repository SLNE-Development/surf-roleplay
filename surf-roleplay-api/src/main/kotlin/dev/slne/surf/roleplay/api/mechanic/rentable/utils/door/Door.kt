package dev.slne.surf.roleplay.api.mechanic.rentable.utils.door

import dev.slne.surf.roleplay.api.mechanic.rentable.Rentable
import dev.slne.surf.roleplay.api.mechanic.rentable.utils.Crackable
import dev.slne.surf.roleplay.api.mechanic.rentable.utils.Lockable
import org.bukkit.Location

interface Door : Lockable, Crackable {

    /**
     * The rentable property that this door belongs to.
     */
    val rentable: Rentable

    /**
     * The door container of this door, which holds multiple doors.
     */
    val container: DoorContainer

    /**
     * The location of the door in the world.
     */
    val location: Location

    /**
     * Opens the door.
     */
    suspend fun open()

    /**
     * Closes the door.
     */
    suspend fun close()
}
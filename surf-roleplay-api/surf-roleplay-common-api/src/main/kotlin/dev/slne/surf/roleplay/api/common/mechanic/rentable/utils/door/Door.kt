package dev.slne.surf.roleplay.api.common.mechanic.rentable.utils.door

import dev.slne.surf.roleplay.api.common.mechanic.rentable.Rentable
import dev.slne.surf.roleplay.api.common.mechanic.rentable.utils.Crackable
import dev.slne.surf.roleplay.api.common.mechanic.rentable.utils.Lockable
import org.bukkit.Location

/**
 * Represents a door in the game that can be locked and cracked.
 * A door is associated with a rentable property and has a specific location in the world.
 */
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
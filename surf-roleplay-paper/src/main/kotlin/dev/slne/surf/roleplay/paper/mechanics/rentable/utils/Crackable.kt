package dev.slne.surf.roleplay.paper.mechanics.rentable.utils

import dev.slne.surf.roleplay.paper.mechanics.rentable.lockpick.LockPick
import dev.slne.surf.roleplay.core.common.player.RpPlayer
import kotlin.time.Duration

/**
 * Represents an entity that can be cracked, such as a lock on a door or a vault.
 * This interface defines the properties and methods required for cracking functionality.
 */
interface Crackable {

    /**
     * The duration it takes to crack the lock of this lockable.
     */
    val crackDuration: Duration

    /**
     * Checks if the lock of this lockable is currently cracked.
     *
     * @return `true` if the lock is cracked, `false` otherwise.
     */
    val cracked: Boolean

    /**
     * Attempts to crack the lock of this lockable using a [LockPick].
     *
     * @param player The player attempting to crack the lock.
     * @param lockPick The lock pick being used to crack the lock.
     * @return The result of the cracking attempt, which can indicate success or failure.
     */
    suspend fun crack(player: RpPlayer, lockPick: LockPick): LockPick.LockPickResult
}
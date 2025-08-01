package dev.slne.surf.roleplay.api.player

import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.*

interface RpPlayer {

    /**
     * The [UUID] of the player
     */
    val uuid: UUID

    /**
     * The username of the player.
     */
    val username: String?

    /**
     * The Bukkit [Player] representation of this player, if they are online.
     * Returns `null` if the player is not online.
     */
    val bukkitPlayer: Player?

    /**
     * The Bukkit [OfflinePlayer] representation of this player.
     */
    val bukkitOfflinePlayer: OfflinePlayer

    companion object {
        /**
         * Gets a [RpPlayer] by their [UUID].
         */
        operator fun get(uuid: UUID) = RpPlayerManager[uuid]
    }

}
package dev.slne.surf.roleplay.api.mechanic.map.vote

import dev.slne.surf.roleplay.api.mechanic.map.Map
import dev.slne.surf.roleplay.api.player.RpPlayer
import it.unimi.dsi.fastutil.objects.ObjectList
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration

/**
 * Represents a map vote mechanic in the game.
 */
interface MapVote {

    /**
     * The votes cast by players during the map voting process.
     */
    val votes: ConcurrentHashMap<Map, ObjectList<UUID>>

    /**
     * The duration of the voting period.
     */
    val votingDuration: Duration

    /**
     * Starts the map voting process.
     */
    suspend fun startVote()

    /**
     * Ends the map voting process.
     *
     * @param reason The reason for ending the vote
     * @param winningMap The map that received the most votes, along with the number of votes it received.
     */
    suspend fun endVote(reason: MapVoteEndedReason)

    /**
     * Casts a vote for a specific map by a player.
     *
     * @param player The player casting the vote.
     * @param map The map that the player is voting for.
     * @param modifier An optional modifier function that can be used to modify the vote count for the player.
     */
    suspend fun castVote(player: RpPlayer, map: Map, modifier: (RpPlayer) -> Int = { 1 })

    /**
     * Retrieves the current status of the map vote.
     *
     * @return The current status of the map vote.
     */
    sealed class MapVoteEndedReason {

        /**
         * Retrieves the map and the number of votes from the reason for ending the vote.
         *
         * @return A pair containing the selected map and the number of votes it received.
         */
        fun getMapFromReason(): Pair<Map?, Int> = when (this) {
            is MapSelected -> map to votes
            is NoVotes -> null to 0
            is AdminCancelled -> null to 0
            is EventCancelled -> null to 0
        }

        /**
         * Represents a scenario where the map vote ended successfully with a selected map.
         *
         * @property map The map that received the most votes.
         * @property votes The number of votes that the selected map received.
         */
        data class MapSelected(val map: Map, val votes: Int) : MapVoteEndedReason()

        /**
         * Represents a scenario where the map vote ended without any votes being cast.
         */
        data object NoVotes : MapVoteEndedReason()

        /**
         * Represents a scenario where the map vote was cancelled by an admin
         */
        data object AdminCancelled : MapVoteEndedReason()

        /**
         * Represents a scenario where the map vote was cancelled by an event
         */
        data class EventCancelled(val reason: String) : MapVoteEndedReason()
    }

}
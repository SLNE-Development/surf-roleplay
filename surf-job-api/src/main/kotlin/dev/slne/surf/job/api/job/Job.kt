package dev.slne.surf.job.api.job

import dev.slne.surf.job.api.player.JobPlayer
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.text.Component

interface Job {

    /**
     * The name of the job, only used in internal checks
     */
    val name: String

    /**
     * The display name of the job used for displaying purposes
     */
    val displayName: Component

    /**
     * The income of the job
     */
    val income: Int

    /**
     * The amount of players that can accept the job at once
     * If the value is -1, it means that there is no limit on the number of players
     */
    val maxPlayers: Int

    /**
     * The requirements of the job that have to be met in order for a player to accept the job
     */
    val joinRequirements: ObjectSet<JobRequirement>

    /**
     * The requirements of the job that have to be met in order for the players in the job to keep the job after some changes, eg. a player leaving the server, etc.
     */
    val keepRequirements: ObjectSet<JobRequirement>

    /**
     * The players that are currently in the job
     */
    val players: ObjectSet<JobPlayer>

    /**
     * Checks if a player can join the job based on the join requirements
     * and the maximum number of players allowed in the job.
     *
     * @param player the [JobPlayer] to check if they can join the job
     * @return true if the player can join the job, false otherwise
     */
    fun canJoin(player: JobPlayer): Boolean

    /**
     * Checks if a player can keep the job based on the keep requirements
     *
     * @param player the [JobPlayer] to check if they can keep the job
     * @return true if the player can keep the job, false otherwise
     */
    fun canKeep(player: JobPlayer): Boolean

    /**
     * Assigns a player to the job.
     *
     * This method sets the player's current job to this job.
     * It is expected that the player has already been checked against the job's requirements.
     *
     * This method also sets the players inventory contents and abilities
     *
     * @param player the [JobPlayer] to assign to the job
     */
    fun assignPlayer(player: JobPlayer)
}
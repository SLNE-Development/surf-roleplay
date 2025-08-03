package dev.slne.surf.job.api.job

import dev.slne.surf.job.api.player.JobPlayer
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.text.Component
import java.util.*

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
     * Formats the income of the job to a localized string representation.
     * This method uses the German locale for formatting, which uses a comma as the decimal separator
     *
     * @param locale the [Locale] to use for formatting the income
     * @return a string representation of the income formatted according to the German locale
     */
    fun formatIncome(locale: Locale): String

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
    fun assignPlayer(player: JobPlayer): Boolean

    /**
     * Removes a player from the job.
     * This method sets the player's current job to citizen.
     *
     * @param player the [JobPlayer] to remove from the job
     * @return true if the player was successfully removed, false otherwise
     */
    fun removePlayer(player: JobPlayer): Boolean

    /**
     * Checks if all players in the job still meet their keep requirements.
     *
     * If not, the players will be removed from the job.
     * @return true if all players meet their keep requirements, false otherwise
     */
    fun performKeepRequirementsCheck(): Boolean
}
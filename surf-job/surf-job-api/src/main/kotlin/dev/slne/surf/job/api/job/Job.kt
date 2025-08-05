package dev.slne.surf.job.api.job

import dev.slne.surf.job.api.player.JobPlayer
import dev.slne.surf.surfapi.bukkit.api.builder.LoreBuilder
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import it.unimi.dsi.fastutil.objects.ObjectList
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import java.util.*

interface Job : ComponentLike {

    /**
     * The category of the job, used for grouping jobs together
     */
    val category: JobCategory

    /**
     * The name of the job, only used in internal checks
     */
    val name: String

    /**
     * The display name of the job used for displaying purposes
     */
    val displayName: Component

    /**
     * The description of the job, used for displaying purposes
     * This is a lambda that builds a [LoreBuilder] which can be used to create a lore for the job
     */
    val description: LoreBuilder.() -> Unit

    /**
     * The rules of the job, used for displaying purposes
     * This is a lambda that builds a [LoreBuilder] which can be used to create a lore for the job
     */
    val rules: LoreBuilder.() -> Unit

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
    fun formatIncome(locale: Locale?): String

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
     * The permission required to join the job.
     */
    val permission: String

    /**
     * Checks if a player can join the job based on the join requirements
     * and the maximum number of players allowed in the job.
     *
     * @param player the [JobPlayer] to check if they can join the job
     * @return a list of [JobRequirement] that the player does not meet, or an empty list if the player meets all requirements
     */
    fun canJoin(player: JobPlayer): ObjectList<JobRequirement>

    /**
     * Checks if a player can keep the job based on the keep requirements
     *
     * @param player the [JobPlayer] to check if they can keep the job
     * @return a list of [JobRequirement] that the player does not meet, or an empty list if the player meets all requirements
     */
    fun canKeep(player: JobPlayer): ObjectList<JobRequirement>

    /**
     * Assigns a player to the job.
     *
     * This method sets the player's current job to this job.
     * It is expected that the player has already been checked against the job's requirements.
     *
     * This method also sets the players inventory contents and abilities
     *
     * @param player the [JobPlayer] to assign to the job
     * @return a [JobChangeResult] indicating the result of the operation.
     */
    fun assignPlayer(player: JobPlayer): JobChangeResult

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
     * If not, the players will be removed from the job.
     *
     * @return true if all players still meet their keep requirements, false otherwise
     */
    fun performKeepRequirementsCheck(): Boolean

    /**
     * Represents the result of a job change operation.
     *
     * @property message a lambda function that takes a [SurfComponentBuilder] and a [Boolean] indicating whether to show the header,
     *                 and appends a message to the builder based on the result of the job change
     */
    sealed class JobChangeResult(val message: (Boolean) -> Component) {
        /**
         * Represents a successful job change operation.
         */
        data object Success : JobChangeResult({ buildText { } })

        /**
         * Represents a job change operation that failed because the player is already in the job.
         */
        data object AlreadyInJob : JobChangeResult({
            buildText {
                error("Du kannst diesem Job nicht beitreten, da du bereits in diesem Job bist.")
            }
        })

        /**
         * Represents a job change operation that failed because the requirements for joining the job are not met.
         *
         * @param requirements The set of job requirements that were not met.
         */
        data class JoinRequirementsNotMet(
            val requirements: ObjectList<JobRequirement>
        ) : JobChangeResult({ showHeader ->
            buildText {
                if (showHeader) {
                    error("Du kannst diesem Job nicht beitreten, da die folgenden Anforderungen nicht erfüllt sind:")
                    appendNewline()
                }

                requirements.forEachIndexed { index, requirement ->
                    if (index > 0) {
                        appendNewline()
                    }

                    val components = LoreBuilder().apply(requirement.description).build()
                    components.forEachIndexed { index, component ->
                        if (index > 0) {
                            appendNewline()
                        }

                        spacer("— ")
                        append(component)
                    }
                }
            }
        })
    }
}
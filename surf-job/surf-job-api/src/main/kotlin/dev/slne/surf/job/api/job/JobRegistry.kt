package dev.slne.surf.job.api.job

import dev.slne.surf.surfapi.core.api.util.requiredService
import it.unimi.dsi.fastutil.objects.ObjectSet
import org.jetbrains.annotations.Unmodifiable

private val jobRegistry = requiredService<JobRegistry>()

interface JobRegistry {

    /**
     * A set of all registered jobs in the job registry.
     */
    val jobs: @Unmodifiable ObjectSet<Job>

    /**
     * Returns the job of the specified class if it exists, or null if it does not.
     *
     * @param jobClass The class of the job to retrieve.
     * @return The job instance of the specified class, or throws an exception if it either does not exist or is not registered.
     */
    fun <T : Job> getJob(jobClass: Class<T>): T

    /**
     * Checks if all jobs still meet their keep requirements.
     * If not, the players will be removed from the job.
     *
     * @return true if all jobs meet their keep requirements, false otherwise
     */
    fun checkJobKeepRequirements(): Boolean

    companion object : JobRegistry by jobRegistry {
        /**
         * The singleton instance of the [JobRegistry].
         */
        val INSTANCE get() = jobRegistry
    }
}

/**
 * Extension function to retrieve a job of the specified type from the [JobRegistry].
 *
 * @return The job instance of the specified type, or null if it does not exist.
 */
inline fun <reified T : Job> JobRegistry.getJob(): T = getJob(T::class.java)
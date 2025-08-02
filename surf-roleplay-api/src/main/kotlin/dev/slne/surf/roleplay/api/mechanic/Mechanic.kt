package dev.slne.surf.roleplay.api.mechanic

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import it.unimi.dsi.fastutil.objects.ObjectSet
import org.bukkit.event.Listener

interface Mechanic {

    /**
     * Returns the name of the mechanic.
     * This name should be unique across all mechanics.
     */
    val name: String

    /**
     * Returns an object set of handlers for this mechanic.
     */
    val handlers: ObjectSet<Listener>

    /**
     * Called when the mechanic is loaded.
     */
    fun onLoad(plugin: SuspendingJavaPlugin)

    /**
     * Called when the mechanic is enabled.
     */
    fun onEnable(plugin: SuspendingJavaPlugin)

    /**
     * Called when the mechanic is disabled.
     */
    fun onDisable(plugin: SuspendingJavaPlugin)
}
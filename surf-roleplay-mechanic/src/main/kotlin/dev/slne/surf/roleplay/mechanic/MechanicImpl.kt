package dev.slne.surf.roleplay.mechanic

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.roleplay.api.mechanic.Mechanic
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import it.unimi.dsi.fastutil.objects.ObjectSet
import org.bukkit.event.Listener
import org.jetbrains.exposed.sql.Table

abstract class MechanicImpl(
    override val name: String,
    override val handlers: ObjectSet<Listener> = objectSetOf()
) : Mechanic {

    open fun getDatabaseTables(): ObjectSet<Table> = objectSetOf()

    override fun onLoad(plugin: SuspendingJavaPlugin) {}
    override fun onEnable(plugin: SuspendingJavaPlugin) {}
    override fun onDisable(plugin: SuspendingJavaPlugin) {}
}
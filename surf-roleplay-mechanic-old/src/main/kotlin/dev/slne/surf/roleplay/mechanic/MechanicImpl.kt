package dev.slne.surf.roleplay.mechanic

import com.github.retrooper.packetevents.event.PacketListener
import com.github.retrooper.packetevents.event.PacketListenerPriority
import dev.slne.surf.roleplay.api.coroutine.RpJob
import dev.slne.surf.roleplay.api.mechanic.Mechanic
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import it.unimi.dsi.fastutil.objects.ObjectSet
import org.bukkit.event.Listener
import org.jetbrains.exposed.sql.Table

abstract class MechanicImpl(
    override val name: String,
    override val handlers: ObjectSet<Listener> = objectSetOf(),
    override val rpJobs: ObjectSet<RpJob> = objectSetOf(),
    override val packetListeners: ObjectSet<Pair<PacketListener, PacketListenerPriority>> = objectSetOf()
) : Mechanic {
    open fun getDatabaseTables(): ObjectSet<Table> = objectSetOf()

    override suspend fun onLoad() {}
    override suspend fun onEnable() {}
    override suspend fun onDisable() {}
}
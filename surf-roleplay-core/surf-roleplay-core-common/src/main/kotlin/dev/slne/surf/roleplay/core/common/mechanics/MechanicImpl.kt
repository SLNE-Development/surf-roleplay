package dev.slne.surf.roleplay.core.common.mechanics

import com.github.retrooper.packetevents.event.PacketListener
import com.github.retrooper.packetevents.event.PacketListenerPriority
import dev.slne.surf.roleplay.api.common.mechanic.Mechanic
import dev.slne.surf.roleplay.api.common.util.RpJob
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import it.unimi.dsi.fastutil.objects.ObjectSet

abstract class MechanicImpl(
    override val name: String,
    override val handlers: ObjectSet<Listener> = objectSetOf(),
    override val rpJobs: ObjectSet<RpJob> = objectSetOf(),
    override val packetListeners: ObjectSet<Pair<PacketListener, PacketListenerPriority>> = objectSetOf()
) : Mechanic {
    override suspend fun onLoad() {}
    override suspend fun onEnable() {}
    override suspend fun onDisable() {}
}
package dev.slne.surf.roleplay.mechanic

import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.surfapi.core.api.util.objectSetOf
import it.unimi.dsi.fastutil.objects.ObjectSet
import org.bukkit.event.Listener

abstract class Mechanic(
    val name: String,
    val handlers: ObjectSet<Listener> = objectSetOf(),
    val rpPlayerDisconnectHook: ((RpPlayer) -> Unit)? = null
) {

    open fun onLoad() {}
    open fun onEnable() {}
    open fun onDisable() {}

}
package dev.slne.surf.roleplay.mechanic

import it.unimi.dsi.fastutil.objects.ObjectSet
import org.bukkit.event.Listener

abstract class Mechanic(
    val name: String,
) {

    abstract fun registerHandlers(handlerList: ObjectSet<Listener>)

    fun onLoad() {}
    fun onEnable() {}
    fun onDisable() {}

}
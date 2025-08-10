package dev.slne.surf.roleplay.api.paper.events

import org.bukkit.Bukkit

open class CancellableRpEvent(
    async: Boolean = !Bukkit.isPrimaryThread()
) : RpEvent(async)
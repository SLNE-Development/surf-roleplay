package dev.slne.surf.roleplay.api.mechanic.jobwages.event

import dev.slne.surf.roleplay.api.events.CancellableEvent
import dev.slne.surf.roleplay.api.player.RpPlayer
import org.bukkit.Bukkit

class PlayerPaycheckEvent(
    val player: RpPlayer,
    var amount: Int,
    async: Boolean = !Bukkit.isPrimaryThread()
) : CancellableEvent(async)
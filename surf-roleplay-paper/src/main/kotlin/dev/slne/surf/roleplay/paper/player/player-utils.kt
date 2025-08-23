package dev.slne.surf.roleplay.paper.player

import dev.slne.surf.roleplay.core.common.player.RpPlayer
import org.bukkit.OfflinePlayer

val OfflinePlayer.rpPlayer get() = RpPlayer[uniqueId] as PaperRpPlayer

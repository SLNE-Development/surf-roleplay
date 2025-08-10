package dev.slne.surf.roleplay.api.paper.player

import dev.slne.surf.cloud.api.client.paper.player.toCloudOfflinePlayer
import dev.slne.surf.roleplay.api.common.player.RpPlayer
import org.bukkit.OfflinePlayer

val OfflinePlayer.rpPlayer get() = RpPlayer[this.toCloudOfflinePlayer()]
package dev.slne.surf.roleplay.mod.keybind

import net.minecraft.client.network.ClientPlayerEntity

fun interface KeybindAction {
    fun handle(player: ClientPlayerEntity)
}
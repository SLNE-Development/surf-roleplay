package dev.slne.roleplay.mod.keybind

import net.minecraft.client.network.ClientPlayerEntity

fun interface KeybindAction {
    fun handle(player: ClientPlayerEntity)
}
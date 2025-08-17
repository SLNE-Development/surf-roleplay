package dev.slne.roleplay.mod.keybind

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.text.Text
import org.lwjgl.glfw.GLFW

enum class Keybinds(
    val key: Int,
    val translationKey: String,
    val category: KeybindCategory,
    val action: KeybindAction,
    val actionExecutableInGui: Boolean = false,
    val type: InputUtil.Type = InputUtil.Type.KEYSYM,
) {
    TEST(
        GLFW.GLFW_KEY_N,
        "key.roleplay.test",
        KeybindCategory.TEST,
        { player ->
            player.sendMessage(Text.literal("Test keybind pressed!"), false)
        }
    ),
    TEST2(
        GLFW.GLFW_KEY_Z,
        "key.roleplay.test2",
        KeybindCategory.TEST,
        { player ->
            player.sendMessage(Text.literal("Test2 keybind pressed!"), false)
        }
    );

    companion object {
        fun registerKeybindings() {
            val registeredKeybinds = entries.associateWith {
                KeyBindingHelper.registerKeyBinding(
                    KeyBinding(
                        it.translationKey,
                        it.type,
                        it.key,
                        it.category.translationKey
                    )
                )
            }

            ClientTickEvents.END_CLIENT_TICK.register { client ->
                registeredKeybinds.forEach { (keybind, keybinding) ->
                    if (keybinding.wasPressed()) {
                        val player = client.player ?: return@forEach

                        if (player.currentScreenHandler != player.playerScreenHandler && !keybind.actionExecutableInGui) {
                            return@forEach
                        }

                        keybind.action.handle(player)
                    }
                }
            }
        }
    }

}
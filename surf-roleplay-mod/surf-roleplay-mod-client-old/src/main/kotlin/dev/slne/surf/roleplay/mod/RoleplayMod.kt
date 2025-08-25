package dev.slne.surf.roleplay.mod

import dev.slne.surf.roleplay.mod.common.network.PacketRegistry
import dev.slne.surf.roleplay.mod.common.network.user.userManager
import dev.slne.surf.roleplay.mod.keybind.Keybinds
import me.mrnavastar.protoweaver.client.ProtoClient
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.minecraft.client.MinecraftClient
import java.util.*

object RoleplayMod : ClientModInitializer {
    var client: ProtoClient? = null

    val uuid: UUID get() = MinecraftClient.getInstance().gameProfile.id

    override fun onInitializeClient() {
        Keybinds.registerKeybindings()

        ClientPlayConnectionEvents.JOIN.register { handler, sender, client ->
            val server = client.server!!
            this.client = ProtoClient(server.serverIp, server.serverPort).also {
                it.connect(PacketRegistry.loginProtocol)
                userManager.init(uuid, it.connection)
            }
        }

        ClientPlayConnectionEvents.DISCONNECT.register { handler, client ->
            this.client?.disconnect()
            this.client = null
            userManager.destroy(uuid)
        }
    }
}
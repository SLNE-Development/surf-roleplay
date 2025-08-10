package dev.slne.surf.roleplay.paper.mechanics.idcard

import dev.slne.surf.npc.api.dsl.npc
import dev.slne.surf.npc.api.surfNpcApi
import dev.slne.surf.roleplay.paper.plugin

object IdCardNpc {
    const val NPC_NAME = "idcard_npc"

    suspend fun spawnNpc() {
        val npcSkin = surfNpcApi.getSkin("MikeyLLP")

        npc(plugin) {
            uniqueName = NPC_NAME
            displayName = {
                primary("Bürgeramt")
            }

            skin = npcSkin

            location {
                world = "world"
                x = 2.0
                y = 100.0
                z = 0.0
            }
        }
    }
}
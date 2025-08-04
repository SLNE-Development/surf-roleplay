package dev.slne.surf.roleplay.mechanic.mechanics.idcard

import dev.slne.surf.npc.api.dsl.npc
import dev.slne.surf.npc.api.surfNpcApi

object IdCardNpc {
    suspend fun spawnNpc() {
        val npcSkin = surfNpcApi.getSkin("MikeyLLP")

        npc {
            uniqueName = IdCardMechanicImpl.NPC_NAME
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
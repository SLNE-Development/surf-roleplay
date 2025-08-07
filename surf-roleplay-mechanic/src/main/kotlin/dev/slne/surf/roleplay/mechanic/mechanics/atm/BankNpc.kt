package dev.slne.surf.roleplay.mechanic.mechanics.atm

import dev.slne.surf.npc.api.dsl.npc
import dev.slne.surf.npc.api.surfNpcApi

object BankNpc {
    suspend fun bankNpc() {
        val npcSkin = surfNpcApi.getSkin("Jo_field")

        npc {
            uniqueName = AtmMechanicImpl.NPC_NAME
            displayName = {
                primary("Bankberater")
            }

            skin = npcSkin

            location {
                world = "world"
                x = 4.0
                y = 100.0
                z = 0.0
            }
        }
    }
}
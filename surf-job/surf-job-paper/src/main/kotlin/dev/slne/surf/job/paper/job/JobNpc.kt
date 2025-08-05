package dev.slne.surf.job.paper.job

import dev.slne.surf.npc.api.dsl.npc
import dev.slne.surf.npc.api.surfNpcApi

object JobNpc {

    const val NPC_NAME = "job_npc"

    val npc get() = surfNpcApi.getNpc(NPC_NAME) ?: error("NPC with name $NPC_NAME not found")

    suspend fun spawnNpc() {
        val npcSkin = surfNpcApi.getSkin("Twisti_twixi")

        npc {
            uniqueName = NPC_NAME
            displayName = {
                primary("Job System")
            }

            skin = npcSkin

            location {
                world = "world"
                x = 0.0
                y = 100.0
                z = 2.0
            }
        }
    }

}
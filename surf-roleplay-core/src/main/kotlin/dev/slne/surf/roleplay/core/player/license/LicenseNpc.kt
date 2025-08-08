package dev.slne.surf.roleplay.core.player.license

import dev.slne.surf.npc.api.dsl.npc
import dev.slne.surf.npc.api.surfNpcApi
import dev.slne.surf.roleplay.core.plugin

object LicenseNpc {

    const val NPC_NAME = "license_npc"

    suspend fun spawnNpc() {
        val npcSkin = surfNpcApi.getSkin("CastCrafter")

        npc(plugin) {
            uniqueName = NPC_NAME
            displayName = {
                primary("Lizenzhändler")
            }

            skin = npcSkin

            location {
                world = "world"
                x = 0.0
                y = 100.0
                z = 0.0
            }
        }
    }
}
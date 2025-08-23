package dev.slne.surf.roleplay.paper.player.license

import dev.slne.surf.npc.api.dsl.npc
import dev.slne.surf.npc.api.surfNpcApi
import dev.slne.surf.roleplay.core.common.RpLifecycle
import dev.slne.surf.roleplay.paper.plugin
import org.springframework.stereotype.Component

@Component
class LicenseNpc : RpLifecycle {

    companion object {
        const val NPC_NAME = "license_npc"
    }

    override suspend fun onEnable() {
        spawnNpc()
    }

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
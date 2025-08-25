package dev.slne.surf.roleplay.paper.mechanics.idcard

import dev.slne.surf.roleplay.paper.mechanics.AbstractMechanic
import dev.slne.surf.roleplay.paper.mechanics.Mechanic

@Mechanic("IdCardMechanic")
class IdCardMechanic : AbstractMechanic() {
    override suspend fun onEnable() {
        IdCardNpc.spawnNpc()
    }
}
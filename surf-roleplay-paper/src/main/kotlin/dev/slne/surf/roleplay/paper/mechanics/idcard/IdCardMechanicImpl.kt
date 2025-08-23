package dev.slne.surf.roleplay.paper.mechanics.idcard

import dev.slne.surf.roleplay.paper.mechanics.AbstractMechanic
import dev.slne.surf.roleplay.paper.mechanics.Mechanic

@Mechanic
class IdCardMechanicImpl : AbstractMechanic("IdCardMechanic") {
    override suspend fun onEnable() {
        IdCardNpc.spawnNpc()
    }
}
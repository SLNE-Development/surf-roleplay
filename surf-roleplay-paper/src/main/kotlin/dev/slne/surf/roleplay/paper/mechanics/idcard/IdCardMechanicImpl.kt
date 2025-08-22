package dev.slne.surf.roleplay.paper.mechanics.idcard

import dev.slne.surf.roleplay.core.common.mechanics.idcard.IdCardMechanic
import dev.slne.surf.roleplay.core.common.mechanics.utils.RpMechanic

@RpMechanic
class IdCardMechanicImpl : IdCardMechanic() {
    override suspend fun onEnable() {
        IdCardNpc.spawnNpc()
    }
}
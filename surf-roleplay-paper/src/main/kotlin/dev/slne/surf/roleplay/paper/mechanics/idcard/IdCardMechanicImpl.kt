package dev.slne.surf.roleplay.paper.mechanics.idcard

import dev.slne.surf.roleplay.core.common.mechanics.idcard.IdCardCommonMechanic
import dev.slne.surf.roleplay.core.common.mechanics.utils.RpMechanic

@RpMechanic
class IdCardMechanicImpl : IdCardCommonMechanic() {
    override suspend fun onEnable() {
        IdCardNpc.spawnNpc()
    }
}
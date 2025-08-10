package dev.slne.surf.roleplay.core.common.mechanics.idcard

import dev.slne.surf.roleplay.api.common.mechanic.idcard.IdCardMechanic
import dev.slne.surf.roleplay.core.common.mechanics.MechanicImpl
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object IdCardMechanicImpl : MechanicImpl(
    name = "IdCardMechanic",
    handlers = objectSetOf(
        IdCardHandler
    )
), IdCardMechanic {

    const val NPC_NAME = "ID-Card_npc"

    override suspend fun onEnable() {
        IdCardNpc.spawnNpc()
    }
}
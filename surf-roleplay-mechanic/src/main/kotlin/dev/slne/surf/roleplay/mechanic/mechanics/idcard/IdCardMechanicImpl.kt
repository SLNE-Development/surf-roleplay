package dev.slne.surf.roleplay.mechanic.mechanics.idcard

import dev.slne.surf.roleplay.api.mechanic.idcard.IdCardMechanic
import dev.slne.surf.roleplay.mechanic.MechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanics.idcard.listeners.IdCardHandler
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object IdCardMechanicImpl : MechanicImpl(
    name = "IdCardMechanic",
    handlers = objectSetOf(
        IdCardHandler
    )
), IdCardMechanic {
    override suspend fun onEnable() {
        IdCardNpc.spawnNpc()
    }
}
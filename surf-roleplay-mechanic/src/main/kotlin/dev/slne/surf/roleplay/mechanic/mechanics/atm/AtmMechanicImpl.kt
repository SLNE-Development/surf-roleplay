package dev.slne.surf.roleplay.mechanic.mechanics.atm

import dev.slne.surf.roleplay.api.mechanic.atm.AtmMechanic
import dev.slne.surf.roleplay.mechanic.MechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanics.atm.listeners.AtmHandler
import dev.slne.surf.roleplay.mechanic.mechanics.atm.listeners.NpcInteractHandler
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object AtmMechanicImpl : MechanicImpl(
    name = "AtmMechanic",
    handlers = objectSetOf(
        AtmHandler,
        NpcInteractHandler
    )
), AtmMechanic {
    const val ATM_VERSION = "v1.0"
    const val NPC_VERSION = "v1.0"
    const val NPC_NAME = "Bank_npc"

    override suspend fun onEnable() {
        BankNpc.bankNpc()
    }
}
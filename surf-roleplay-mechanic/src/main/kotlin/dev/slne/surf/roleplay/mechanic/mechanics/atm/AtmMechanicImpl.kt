package dev.slne.surf.roleplay.mechanic.mechanics.atm

import dev.slne.surf.roleplay.api.mechanic.atm.AtmMechanic
import dev.slne.surf.roleplay.mechanic.MechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanics.atm.listeners.AtmHandler
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object AtmMechanicImpl : MechanicImpl(
    name = "AtmMechanic",
    handlers = objectSetOf(
        AtmHandler
    )
), AtmMechanic {
    const val VERSION = "v1.0"
}
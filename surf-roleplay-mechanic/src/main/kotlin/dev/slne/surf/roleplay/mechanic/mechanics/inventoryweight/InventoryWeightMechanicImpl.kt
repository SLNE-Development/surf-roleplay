package dev.slne.surf.roleplay.mechanic.mechanics.inventoryweight

import dev.slne.surf.roleplay.api.mechanic.inventoryweight.InventoryWeightMechanic
import dev.slne.surf.roleplay.mechanic.MechanicImpl
import dev.slne.surf.roleplay.mechanic.mechanics.inventoryweight.listeners.InventoryHandler
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object InventoryWeightMechanicImpl : MechanicImpl(
    name = "InventoryWeightMechanic",
    handlers = objectSetOf(
        InventoryHandler
    )
), InventoryWeightMechanic
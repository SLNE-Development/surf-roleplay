package dev.slne.surf.roleplay.mechanic.mechanics.rentable.utils

import dev.slne.surf.roleplay.api.mechanic.rentable.utils.LockPick
import dev.slne.surf.roleplay.api.player.RpPlayer
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder

abstract class LockPickImpl(
    override val displayName: SurfComponentBuilder.() -> Unit,
    override val breakChance: (RpPlayer) -> Double
) : LockPick
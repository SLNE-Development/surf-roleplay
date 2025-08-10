package dev.slne.surf.roleplay.core.common.mechanics.rentable.lockpick

import dev.slne.surf.roleplay.api.common.mechanic.rentable.utils.LockPick
import dev.slne.surf.roleplay.api.common.player.RpPlayer
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder

abstract class LockPickImpl(
    override val displayName: SurfComponentBuilder.() -> Unit,
    override val breakChance: (RpPlayer) -> Double
) : LockPick
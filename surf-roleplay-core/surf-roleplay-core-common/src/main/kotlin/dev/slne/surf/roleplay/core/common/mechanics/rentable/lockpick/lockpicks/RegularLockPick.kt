package dev.slne.surf.roleplay.core.common.mechanics.rentable.lockpick.lockpicks

import dev.slne.surf.roleplay.core.common.mechanics.rentable.lockpick.LockPickImpl

object RegularLockPick : LockPickImpl(
    displayName = {
        primary("Dietrich")
    },
    breakChance = { 0.5 }
)
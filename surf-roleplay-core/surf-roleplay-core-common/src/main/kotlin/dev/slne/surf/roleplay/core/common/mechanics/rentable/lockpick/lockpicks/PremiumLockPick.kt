package dev.slne.surf.roleplay.core.common.mechanics.rentable.lockpick.lockpicks

import dev.slne.surf.roleplay.core.common.mechanics.rentable.lockpick.LockPickImpl

object PremiumLockPick : LockPickImpl(
    displayName = {
        primary("Verzinkter Vierkantstahl Dietrich")
    },
    breakChance = { 0.1 }
)
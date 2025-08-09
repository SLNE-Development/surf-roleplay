package dev.slne.surf.roleplay.mechanic.mechanics.rentable.utils.lockpick

import dev.slne.surf.roleplay.mechanic.mechanics.rentable.utils.LockPickImpl

object PremiumLockPick : LockPickImpl(
    displayName = {
        primary("Verzinkter Vierkantstahl Dietrich")
    },
    breakChance = { 0.1 }
)
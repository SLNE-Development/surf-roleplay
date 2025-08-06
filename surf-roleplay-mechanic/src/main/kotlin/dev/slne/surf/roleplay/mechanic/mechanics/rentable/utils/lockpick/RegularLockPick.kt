package dev.slne.surf.roleplay.mechanic.mechanics.rentable.utils.lockpick

import dev.slne.surf.roleplay.mechanic.mechanics.rentable.utils.LockPickImpl

object RegularLockPick : LockPickImpl(
    displayName = {
        primary("Dietrich")
    },
    breakChance = { 0.5 }
)
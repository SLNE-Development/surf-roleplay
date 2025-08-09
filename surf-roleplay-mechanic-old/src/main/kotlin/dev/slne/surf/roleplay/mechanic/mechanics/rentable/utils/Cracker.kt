package dev.slne.surf.roleplay.mechanic.mechanics.rentable.utils

import dev.slne.surf.roleplay.api.mechanic.rentable.utils.Crackable
import dev.slne.surf.roleplay.api.mechanic.rentable.utils.LockPick
import dev.slne.surf.roleplay.api.player.RpPlayer

object Cracker {

    fun crack(
        player: RpPlayer,
        crackable: Crackable,
        lockPick: LockPick,
        successAction: () -> Unit
    ): LockPick.LockPickResult {
        successAction()
        return LockPick.LockPickResult.SUCCESS
    }

}
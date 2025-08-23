package dev.slne.surf.roleplay.paper.mechanics.rentable

import dev.slne.surf.roleplay.paper.mechanics.rentable.lockpick.LockPick
import dev.slne.surf.roleplay.paper.mechanics.rentable.utils.Crackable
import dev.slne.surf.roleplay.core.common.player.RpPlayer

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
package dev.slne.surf.roleplay.core.common.mechanics.rentable

import dev.slne.surf.roleplay.api.common.mechanic.rentable.utils.Crackable
import dev.slne.surf.roleplay.api.common.mechanic.rentable.utils.LockPick
import dev.slne.surf.roleplay.api.common.player.RpPlayer

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
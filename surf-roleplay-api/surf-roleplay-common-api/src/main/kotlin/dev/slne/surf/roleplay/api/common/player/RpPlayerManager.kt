package dev.slne.surf.roleplay.api.common.player

import dev.slne.surf.roleplay.api.common.InternalContextHolder
import dev.slne.surf.roleplay.api.common.util.InternalRoleplayApi
import org.springframework.beans.factory.getBean
import java.util.*

@InternalRoleplayApi
interface RpPlayerManager {
    fun getPlayerByUuid(uuid: UUID): RpPlayer

    companion object {
        val instance get() = InternalContextHolder.instance.context.getBean<RpPlayerManager>()
    }
}
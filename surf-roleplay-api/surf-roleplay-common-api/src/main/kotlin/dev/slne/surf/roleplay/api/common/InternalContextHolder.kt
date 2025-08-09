package dev.slne.surf.roleplay.api.common

import dev.slne.surf.roleplay.api.common.util.InternalRoleplayApi
import dev.slne.surf.surfapi.core.api.util.requiredService
import org.springframework.context.ApplicationContext

@InternalRoleplayApi
interface InternalContextHolder {
    val context: ApplicationContext

    companion object {
        val instance = requiredService<InternalContextHolder>()
    }
}
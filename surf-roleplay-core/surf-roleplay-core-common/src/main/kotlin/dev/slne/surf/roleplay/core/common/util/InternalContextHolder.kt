package dev.slne.surf.roleplay.core.common.util

import dev.slne.surf.surfapi.core.api.util.requiredService
import org.springframework.context.ApplicationContext

@InternalRoleplayApi
interface InternalContextHolder {
    val context: ApplicationContext

    companion object {
        val instance = requiredService<InternalContextHolder>()
    }
}
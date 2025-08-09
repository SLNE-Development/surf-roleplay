package dev.slne.surf.roleplay.core.common

import com.google.auto.service.AutoService
import dev.slne.surf.roleplay.api.common.InternalContextHolder
import dev.slne.surf.roleplay.api.common.util.InternalRoleplayApi
import org.springframework.context.ApplicationContext

@OptIn(InternalRoleplayApi::class)
@AutoService(InternalContextHolder::class)
class ContextHolderImpl : InternalContextHolder {
    override lateinit var context: ApplicationContext

    companion object {
        val instance = InternalContextHolder.instance as ContextHolderImpl
    }
}
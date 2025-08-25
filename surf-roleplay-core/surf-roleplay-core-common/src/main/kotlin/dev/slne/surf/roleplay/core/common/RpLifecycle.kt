package dev.slne.surf.roleplay.core.common

import org.springframework.stereotype.Component

@Component
interface RpLifecycle {
    suspend fun onLoad() = Unit
    suspend fun onEnable() = Unit
    suspend fun onDisable() = Unit
}
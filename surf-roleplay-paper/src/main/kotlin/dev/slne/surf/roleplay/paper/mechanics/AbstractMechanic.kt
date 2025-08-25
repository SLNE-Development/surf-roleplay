package dev.slne.surf.roleplay.paper.mechanics

import dev.slne.surf.roleplay.core.common.RpLifecycle

abstract class AbstractMechanic : RpLifecycle {
    private val annotation by lazy {
        javaClass.getAnnotation(Mechanic::class.java) ?: error("@Mechanic annotation is missing!")
    }

    val name by lazy { annotation.name }
}
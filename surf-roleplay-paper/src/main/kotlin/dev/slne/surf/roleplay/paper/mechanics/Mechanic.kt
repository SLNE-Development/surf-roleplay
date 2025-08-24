package dev.slne.surf.roleplay.paper.mechanics

import org.springframework.stereotype.Component

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@Component
annotation class Mechanic(val name: String)

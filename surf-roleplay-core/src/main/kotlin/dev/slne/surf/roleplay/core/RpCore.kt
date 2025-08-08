package dev.slne.surf.roleplay.core

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin

val plugin get() = RpCore.plugin

object RpCore {

    lateinit var plugin: SuspendingJavaPlugin

}
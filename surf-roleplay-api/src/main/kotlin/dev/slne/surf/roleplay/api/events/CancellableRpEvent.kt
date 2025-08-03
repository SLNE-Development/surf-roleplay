package dev.slne.surf.roleplay.api.events

import org.bukkit.event.Cancellable

open class CancellableRpEvent() : RpEvent(), Cancellable {

    private var cancelled = false

    override fun isCancelled() = cancelled

    override fun setCancelled(cancel: Boolean) {
        cancelled = cancel
    }
}
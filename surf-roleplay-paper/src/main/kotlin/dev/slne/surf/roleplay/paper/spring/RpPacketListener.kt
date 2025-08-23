package dev.slne.surf.roleplay.paper.spring

import com.github.retrooper.packetevents.event.PacketListenerPriority
import org.springframework.stereotype.Component

@Component
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class RpPacketListener(
    val priority: PacketListenerPriority = PacketListenerPriority.NORMAL,
)
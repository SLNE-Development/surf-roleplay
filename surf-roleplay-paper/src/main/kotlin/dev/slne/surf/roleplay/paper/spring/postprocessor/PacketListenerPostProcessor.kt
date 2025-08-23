package dev.slne.surf.roleplay.paper.spring.postprocessor

import com.github.retrooper.packetevents.event.PacketListener
import dev.slne.surf.cloud.api.common.util.findAnnotation
import dev.slne.surf.cloud.api.common.util.object2ObjectMapOf
import dev.slne.surf.roleplay.core.common.RpLifecycle
import dev.slne.surf.roleplay.paper.spring.RpPacketListener
import dev.slne.surf.surfapi.core.api.extensions.packetEvents
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component

@Component
class PacketListenerPostProcessor : BeanPostProcessor, RpLifecycle {
    private val packetListeners = object2ObjectMapOf<PacketListener, RpPacketListener>()

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
        if (bean !is PacketListener) return bean

        val annotation = bean::class.findAnnotation<RpPacketListener>()

        if (annotation != null) {
            packetListeners[bean] = annotation
        }

        return bean
    }

    override suspend fun onEnable() {
        val eventManager = packetEvents.eventManager

        packetListeners.forEach { (listener, annotation) ->
            eventManager.registerListener(listener, annotation.priority)
        }
    }
}
package dev.slne.surf.roleplay.paper.spring.postprocessor

import dev.slne.surf.cloud.api.common.util.mutableObjectSetOf
import dev.slne.surf.surfapi.bukkit.api.event.register
import org.bukkit.event.Listener
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component

@Component
class BukkitListenerPostProcessor : BeanPostProcessor {

    private val watched = mutableObjectSetOf<Listener>()

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
        if (bean is Listener) {
            watched.add(bean)
        }

        return bean
    }
    
    fun registerListeners() {
        for (listener in watched) {
            listener.register()
        }
    }
}
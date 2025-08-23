package dev.slne.surf.roleplay.core.common

import dev.slne.surf.cloud.api.common.util.forEachAnnotationOrdered
import dev.slne.surf.cloud.api.common.util.forEachAnnotationOrderedReversed
import org.springframework.beans.factory.ObjectProvider
import org.springframework.stereotype.Component
import javax.annotation.OverridingMethodsMustInvokeSuper

@Component
class RpInstance(private val lifecycles: ObjectProvider<RpLifecycle>) {

    suspend fun onLoad() {
        lifecycles.forEachAnnotationOrdered { it.onLoad() }
    }

    suspend fun onEnable() {
        lifecycles.forEachAnnotationOrdered { it.onEnable() }
    }

    @OverridingMethodsMustInvokeSuper
    suspend fun onDisable() {
        lifecycles.forEachAnnotationOrderedReversed { it.onDisable() }
    }

}
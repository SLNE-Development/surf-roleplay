package dev.slne.surf.roleplay.core.common

import dev.slne.surf.cloud.api.common.util.forEachAnnotationOrdered
import dev.slne.surf.cloud.api.common.util.forEachAnnotationOrderedReversed
import dev.slne.surf.roleplay.core.common.mechanics.MechanicRegistry
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.OverridingMethodsMustInvokeSuper

@Component
class RpInstance(private val lifecycles: ObjectProvider<RpLifecycle>) {
    @Autowired
    private lateinit var mechanicRegistry: MechanicRegistry


    suspend fun onLoad() {
        lifecycles.forEachAnnotationOrdered { it.onLoad() }

        mechanicRegistry.loadMechanics()
    }

    suspend fun onEnable() {
        lifecycles.forEachAnnotationOrdered { it.onEnable() }

        mechanicRegistry.enableMechanics()
    }

    @OverridingMethodsMustInvokeSuper
    suspend fun onDisable() {
        lifecycles.forEachAnnotationOrderedReversed { it.onDisable() }

        mechanicRegistry.disableMechanics()
    }

}
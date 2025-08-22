package dev.slne.surf.roleplay.core.common

import dev.slne.surf.roleplay.core.common.mechanics.MechanicRegistry
import javax.annotation.OverridingMethodsMustInvokeSuper

abstract class RpInstance(
    private val mechanicRegistry: MechanicRegistry
) {

    @OverridingMethodsMustInvokeSuper
    open suspend fun onLoad() {
        mechanicRegistry.loadMechanics()
    }

    @OverridingMethodsMustInvokeSuper
    open suspend fun onEnable() {
        mechanicRegistry.enableMechanics()
    }

    @OverridingMethodsMustInvokeSuper
    open suspend fun onDisable() {
        mechanicRegistry.disableMechanics()
    }

}
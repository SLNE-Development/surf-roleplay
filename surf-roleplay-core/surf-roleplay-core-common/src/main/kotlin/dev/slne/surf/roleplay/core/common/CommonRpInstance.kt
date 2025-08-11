package dev.slne.surf.roleplay.core.common

import dev.slne.surf.roleplay.core.common.mechanics.CommonMechanicRegistry
import javax.annotation.OverridingMethodsMustInvokeSuper

abstract class CommonRpInstance(
    private val commonMechanicRegistry: CommonMechanicRegistry
) {

    @OverridingMethodsMustInvokeSuper
    open suspend fun onLoad() {
        commonMechanicRegistry.loadMechanics()
    }

    @OverridingMethodsMustInvokeSuper
    open suspend fun onEnable() {
        commonMechanicRegistry.enableMechanics()
    }

    @OverridingMethodsMustInvokeSuper
    open suspend fun onDisable() {
        commonMechanicRegistry.disableMechanics()
    }

}
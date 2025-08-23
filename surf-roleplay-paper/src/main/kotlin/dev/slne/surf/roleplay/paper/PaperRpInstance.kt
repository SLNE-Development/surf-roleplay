package dev.slne.surf.roleplay.paper

import dev.slne.surf.roleplay.core.common.RpInstance
import dev.slne.surf.roleplay.paper.spring.postprocessor.BukkitListenerPostProcessor
import dev.slne.surf.roleplay.paper.spring.postprocessor.PacketListenerPostProcessor
import org.springframework.stereotype.Component

@Component
class PaperRpInstance(
    commonMechanicRegistry: MechanicRegistry,
    private val bukkitListenerPostProcessor: BukkitListenerPostProcessor,
    private val packetListenerPostProcessor: PacketListenerPostProcessor
) : RpInstance(commonMechanicRegistry) {

    override suspend fun onEnable() {
        super.onEnable()

        bukkitListenerPostProcessor.registerListeners()
        packetListenerPostProcessor.registerListeners()
    }
}
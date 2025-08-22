package dev.slne.surf.roleplay.paper

import dev.slne.surf.roleplay.core.common.RpInstance
import dev.slne.surf.roleplay.core.common.mechanics.MechanicRegistry
import dev.slne.surf.roleplay.paper.player.license.LicenseNpc
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

        LicenseNpc.spawnNpc()

        bukkitListenerPostProcessor.registerListeners()
        packetListenerPostProcessor.registerListeners()
    }
}
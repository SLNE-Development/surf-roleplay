package dev.slne.surf.roleplay.paper.player

import dev.slne.surf.roleplay.core.common.player.RpPlayer
import dev.slne.surf.roleplay.core.common.player.RpPlayerManager
import dev.slne.surf.roleplay.paper.player.identity.RpIdentity
import dev.slne.surf.surfapi.bukkit.api.extensions.server
import io.papermc.paper.datacomponent.item.attribute.AttributeModifierDisplay.override
import it.unimi.dsi.fastutil.objects.ObjectList
import org.springframework.stereotype.Component
import java.util.*

@Component
class PaperRpPlayerManager : RpPlayerManager() {
    override val players get() = super.players as ObjectList<PaperRpPlayer>

    override fun createPlayer(uuid: UUID) = PaperRpPlayer(uuid)
    override fun onlineUuids() = server.onlinePlayers.map { it.uniqueId }


}
package dev.slne.surf.roleplay.server.player.identity

import dev.slne.surf.roleplay.core.common.player.identity.NetworkIdentity
import dev.slne.surf.roleplay.server.player.identity.db.RpIdentityRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class RpIdentityService(private val rpIdentityRepository: RpIdentityRepository) {
    suspend fun fetchByUuid(uuid: UUID) = rpIdentityRepository.findByUuid(uuid)
    suspend fun createOrUpdateIdentity(identity: NetworkIdentity) =
        rpIdentityRepository.updateOrCreateIdentity(identity.uuid, identity)
}
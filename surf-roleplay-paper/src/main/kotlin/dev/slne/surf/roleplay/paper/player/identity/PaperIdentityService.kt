package dev.slne.surf.roleplay.paper.player.identity

import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PaperIdentityService {

    suspend fun fetchIdentities(uuid: UUID): Set<RpIdentity> {
        TODO("Not yet implemented")
    }

    suspend fun <T : RpIdentity> createIdentity(
        identity: T
    ): T {
        TODO("Not yet implemented")
    }

    suspend fun <T : RpIdentity> updateIdentity(
        identity: T
    ): T? {
        TODO("Not yet implemented")
    }

    suspend fun <T : RpIdentity> createOrUpdateIdentity(
        identity: T
    ): T {
        TODO("Not yet implemented")
    }
}
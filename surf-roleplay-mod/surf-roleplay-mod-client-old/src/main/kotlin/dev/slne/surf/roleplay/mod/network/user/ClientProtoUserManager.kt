package dev.slne.surf.roleplay.mod.network.user

import com.google.auto.service.AutoService
import dev.slne.surf.roleplay.mod.common.network.user.ProtoUser
import dev.slne.surf.roleplay.mod.common.network.user.ProtoUserManager
import me.mrnavastar.protoweaver.api.netty.ProtoConnection
import java.util.*

@AutoService(ProtoUserManager::class)
class ClientProtoUserManager : ProtoUserManager() {
    override fun createUser(
        uuid: UUID,
        connection: ProtoConnection
    ): ProtoUser {
        require(uuid == ClientProtoUser.uuid) { "UUID mismatch: expected ${ClientProtoUser.uuid}, got $uuid" }
        return ClientProtoUser
    }
}